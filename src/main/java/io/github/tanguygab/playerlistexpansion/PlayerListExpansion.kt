package io.github.tanguygab.playerlistexpansion

import io.github.tanguygab.playerlistexpansion.filters.Filter
import io.github.tanguygab.playerlistexpansion.groups.GroupedList
import io.github.tanguygab.playerlistexpansion.groups.ListGroup
import io.github.tanguygab.playerlistexpansion.sorting.SortingType
import me.clip.placeholderapi.expansion.Configurable
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.clip.placeholderapi.expansion.Taskable
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import java.util.logging.Level
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.max

class PlayerListExpansion : PlaceholderExpansion(), Taskable, Configurable {
    private val lists = HashMap<String, PlayerList>()
    private val listGroups = HashMap<String, ListGroup>()
    private val placeholders = ArrayList<String>()

    var updateCooldown: Int = 0
    lateinit var offlineText: String
    lateinit var argumentSeparator: String

    companion object {
        private val FORMAT_PATTERN: Pattern = Pattern.compile("_format:")

        private lateinit var instance: PlayerListExpansion

        private val defaults = LinkedHashMap<String, Any?>().apply {
            put("update-cooldown", 1000)
            put("offline-text", "Offline")
            put("argument-separator", ",")
            put("lists", HashMap<String, Any>().apply {
                put("staff", LinkedHashMap<String, Any>().apply {
                    put("type", "ONLINE")
                    put("included", true)
                    put("filters", listOf("PERMISSION:group.staff", "CANSEE"))
                })
                put("players", LinkedHashMap<String, Any>().apply {
                    put("type", "ONLINE")
                    put("included", true)
                    put("filters", listOf("!PERMISSION:group.staff", "CANSEE"))
                })
            })
            put("groups", HashMap<String, Any>().apply {
                put("staffAndPlayers", LinkedHashMap<String, Any>().apply {
                    put("gap", 1)
                    put("lists", LinkedHashMap<String, Any>().apply {
                        put("staff", LinkedHashMap<String, String>().apply {
                            put("title", "Staff Members (%amount%)")
                            put("max", 10)
                            put("remaining", "... and %remaining% more!")
                        })
                        put("players", LinkedHashMap<String, String>().apply {
                            put("title", "Players (%amount%)")
                        })
                    })
                })
            })
        }

        fun get(): PlayerListExpansion {
            return instance
        }
    }

    init {
        instance = this
    }

    override fun getAuthor() = "Tanguygab"
    override fun getIdentifier() = "playerlist"
    override fun getVersion() = "3.1.0"
    override fun getPlaceholders() = placeholders
    override fun getDefaults() = Companion.defaults

    override fun start() {
        updateCooldown = getInt("update-cooldown", 1000)
        offlineText = getString("offline-text", "Offline")!!
        argumentSeparator = Pattern.quote(getString("argument-separator", ","))

        val config = getConfigSection("lists")
        if (config == null) return
        config.getValues(false).forEach { (list: String, _) ->
            val cfg = getConfigSection("lists.$list")
            if (cfg == null) return@forEach

            val type0: String = cfg.getString("type", "ONLINE")!!
            val type: ListType? = ListType.find(type0)
            if (type == null) {
                log(Level.CONFIG, "List \"$list\" has an invalid type \"$type0\"! Skipping...")
                return@forEach
            }

            val included = cfg.getBoolean("included", true)
            val duplicates = cfg.getBoolean("duplicates", true)

            val filters = ArrayList<Filter>()
            cfg.getStringList("filters").forEach {
                val filter: Filter? = Filter.find(it)
                if (filter == null) {
                    log(
                        Level.CONFIG,
                        "List \"$list\" has an invalid filter type \"$it\"! Skipping..."
                    )
                    return@forEach
                }
                filters.add(filter)
            }

            val sortingTypes = ArrayList<SortingType>()
            cfg.getStringList("sorting-types").forEach { it ->
                val sortingType: SortingType? = SortingType.find(it)
                if (sortingType == null) {
                    log(
                        Level.CONFIG,
                        "List \"$list\" has an invalid sorting type \"$it\"! Skipping..."
                    )
                    return@forEach
                }
                sortingTypes.add(sortingType)
            }
            lists.put(list, PlayerList(list, type, filters, sortingTypes, included, duplicates))
        }
        placeholders.addAll(lists.keys.map { listName: String? -> "%playerlist_" + listName + "_<list|amount|#>%" })


        val groups = getConfigSection("groups")
        if (groups == null) return
        groups.getValues(false).forEach { (group: String, _) ->
            val cfg = getConfigSection("groups.$group")
            if (cfg == null) return@forEach

            val gap = max(0, cfg.getInt("gap", 1))

            val listsCfg = cfg.getConfigurationSection("lists")
            if (listsCfg == null || listsCfg.getValues(false).isEmpty()) {
                log(Level.CONFIG, "Group \"$group\" is empty! Skipping...")
                return@forEach
            }
            val lists = ArrayList<GroupedList>()
            listsCfg.getValues(false).forEach { (list: String, settings0: Any) ->
                if (!this.lists.containsKey(list)) {
                    log(Level.CONFIG, "List \"$list\" in group \"$group\" doesn't exist! Skipping...")
                    return@forEach
                }
                val settings = settings0 as ConfigurationSection

                val title: String = settings.getString("title", "$list (%amount%)")!!
                val max = max(0, settings.getInt("max", 0))
                val remaining = settings.getString("remaining")
                lists.add(GroupedList(this.lists[list]!!, title, max, remaining))
            }

            if (lists.isEmpty()) {
                log(Level.CONFIG, "Group \"$group\" is empty! Skipping...")
                return@forEach
            }
            listGroups.put(group, ListGroup(lists, gap))
        }
        placeholders.addAll(listGroups.keys.map { "%playerlist_group_${it}_<amount|#>%" })
    }

    override fun stop() {}

    override fun onRequest(player: OfflinePlayer?, params: String): String? {
        val matcher: Matcher = FORMAT_PATTERN.matcher(params)
        val uParams = if (matcher.find()) params.substring(0, matcher.start()) else params

        val uIndex = uParams.lastIndexOf('_')
        if (uIndex == -1 || uIndex == params.length - 1) return null

        val list = params.substring(0, uIndex)
        val output = params.substring(uIndex + 1)

        if (list.startsWith("group_")) {
            val group = list.substring(6)
            val groupedList = listGroups[group]
            return groupedList?.getText(player, output)
        }

        val playerList = lists[list]
        return playerList?.getText(player, output)
    }

    fun getFormat(arg: String, args: List<String>): String {
        if (args.size < 2) return "{player_name}"
        val argument = arg.substring(args[0].length + 1)
        return if (argument.startsWith("format:")) argument.substring(7) else "{player_name}"
    }
}
