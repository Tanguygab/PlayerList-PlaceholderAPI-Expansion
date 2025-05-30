package io.github.tanguygab.playerlistexpansion.sorting

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import java.util.function.Function

abstract class SortingType protected constructor(
    private val placeholder: String?
) {

    protected fun parse(name: String, viewer: OfflinePlayer?): String {
        if (placeholder === null || placeholder.isBlank()) return name
        if (!placeholder.contains("%")) return placeholder

        var output = placeholder
        val player = Bukkit.getServer().getOfflinePlayerIfCached(name)
        if (player?.player != null && viewer?.player != null)
            output = PlaceholderAPI.setRelationalPlaceholders(viewer.player, player.player, output)

        output = PlaceholderAPI.setPlaceholders(player, output)
        return output
    }

    abstract fun getSortingString(name: String, viewer: OfflinePlayer?): String

    protected fun parseDouble(number: String?): Double {
        return number?.toDoubleOrNull() ?: 0.0
    }

    companion object {
        protected const val DEFAULT_NUMBER: Int = Int.MAX_VALUE / 2

        private val types = HashMap<String, Function<String?, SortingType>>().apply {
            put("A_TO_Z") { A_TO_Z(it) }
            put("Z_TO_A") { Z_TO_A(it) }
            put("LOW_TO_HIGH") { LOW_TO_HIGH(it) }
            put("HIGH_TO_LOW") { HIGH_TO_LOW(it) }
            put("PLACEHOLDER") { PLACEHOLDER(it!!) }
        }

        fun find(string: String): SortingType? {
            val args = string.split(":", limit = 2)
            val type = args[0].uppercase()
            val arg = if (args.size > 1) args[1] else null
            return if (types.containsKey(type)) types[type]!!.apply(arg) else null
        }
    }
}
