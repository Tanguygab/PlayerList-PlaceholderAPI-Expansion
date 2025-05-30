package io.github.tanguygab.playerlistexpansion.sorting

import io.github.tanguygab.playerlistexpansion.PlayerListExpansion
import org.bukkit.OfflinePlayer

class PLACEHOLDER(arg: String) : SortingType(arg.substring(0, arg.indexOf(":"))) {
    private val sortingMap = LinkedHashMap<String?, Int?>()

    init {
        val arg = arg.substring(arg.indexOf(":") + 1)
        if (arg.isNotEmpty()) {
            val outputs = arg.split(PlayerListExpansion.get().argumentSeparator)
            var index = 1
            outputs.map { it.split("|") }.flatMap { it }.forEach { it
                sortingMap.put(color(it.trim().lowercase()), index++)
            }
        }
    }

    override fun getSortingString(name: String, viewer: OfflinePlayer?): String {
        val output = parse(name, viewer)
        val position = sortingMap.getOrDefault(output, sortingMap.size + 1)!!
        return position.toChar().toString()
    }

    @Suppress("DEPRECATION")
    private fun color(string: String) = net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', string)
}
