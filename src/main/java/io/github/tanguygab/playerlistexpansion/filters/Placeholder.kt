package io.github.tanguygab.playerlistexpansion.filters

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.OfflinePlayer

class Placeholder(arg: String) : Filter() {
    private val placeholders = split(arg).map { it.split("=") }

    override fun filter(name: String?, viewer: OfflinePlayer?): Boolean {
        return placeholders.any { parse(name, viewer, it) }
    }

    private fun parse(name: String?, viewer: OfflinePlayer?, arr: List<String>): Boolean {
        return PlaceholderAPI.setPlaceholders(getOffline(name), arr[0]) ==
                PlaceholderAPI.setPlaceholders(viewer, arr[1])
    }
}
