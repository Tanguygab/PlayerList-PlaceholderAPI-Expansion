package io.github.tanguygab.playerlistexpansion

import io.github.tanguygab.playerlistexpansion.filters.Filter
import io.github.tanguygab.playerlistexpansion.sorting.SortingType
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import java.util.TreeMap
import java.util.WeakHashMap
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.Boolean

class PlayerList(
    private val name: String,
    private val type: ListType,
    private val filters: List<Filter>,
    private val sortingTypes: List<SortingType>,
    private val included: Boolean,
    private val duplicates: Boolean
) {
    private val lastUpdate = WeakHashMap<OfflinePlayer, CachedList>()

    fun getText(viewer: OfflinePlayer?, arguments: String): String? {
        val args = arguments.split("_")
        val format = PlayerListExpansion.get().getFormat(arguments, args)
        val arg = args[0]

        val names = lastUpdate.computeIfAbsent(viewer) { k: OfflinePlayer? ->
            CachedList { viewer: OfflinePlayer?, format: String ->
                getList(
                    viewer,
                    format
                )
            }
        }!!.getList(viewer, format)

        val pos = arg.toIntOrNull()
        return when {
            arg == "amount" -> names.size.toString()
            arg.startsWith("list") -> {
                val separator = if (arg.startsWith("list-")) arg.substring(5).replace("\\n", "\n") else ", "
                return names.joinToString(separator)
            }
            pos === null -> null
            pos >= 0 && pos < names.size -> names[pos]
            else -> PlayerListExpansion.get().offlineText
        }
    }

    fun getList(viewer: OfflinePlayer?, format: String): List<String> {
        var stream: Stream<String>
        if (type == ListType.CUSTOM) {
            val input: String = PlaceholderAPI.setPlaceholders(
                viewer,
                PlayerListExpansion.get().getString("lists.$name.input", "")!!
            )
            if (input.isEmpty()) stream = Stream.of()
            else {
                val separator = PlayerListExpansion.get().getString("lists.$name.separator", ",")!!
                stream = input.split(separator).stream()
            }
        } else stream = type
            .getList()
            .stream()
            .map { obj: OfflinePlayer? -> obj?.name }

        stream = filter(viewer, format, stream)

        return sort(stream.collect(Collectors.toList()), viewer)
    }

    private fun filter(
        viewer: OfflinePlayer?,
        format: String,
        stream: Stream<String>
    ): Stream<String> {
        var stream = stream
        if (!included) stream = stream.filter { it != viewer?.name }
        if (!duplicates) stream = stream.distinct()

        stream = stream.filter { name: String ->
            filters
                .stream()
                .noneMatch { it.isInverted == it.filter(name, viewer) }
        }

        stream = stream.map {
            val player = Bukkit.getOfflinePlayerIfCached(it)
            if (player === null || (!player.hasPlayedBefore() && !player.isOnline)) return@map it
            PlaceholderAPI.setBracketPlaceholders(player, format)
        }

        return stream
    }

    private fun sort(list: List<String>, viewer: OfflinePlayer?): List<String> {
        if (sortingTypes.isEmpty()) return list

        val sortedMap = TreeMap<String, ArrayList<String>>()
        list.forEach { name: String ->
            val sortingString = StringBuilder()
            sortingTypes.forEach { sortingString.append(it.getSortingString(name, viewer)) }
            sortedMap.computeIfAbsent(sortingString.toString()) { k: String -> ArrayList() }.add(name)
        }
        return sortedMap.values
            .stream()
            .flatMap { obj: List<String> -> obj.stream() }
            .collect(Collectors.toList())
    }
}
