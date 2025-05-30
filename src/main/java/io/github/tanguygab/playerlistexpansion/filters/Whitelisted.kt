package io.github.tanguygab.playerlistexpansion.filters

import org.bukkit.OfflinePlayer

class Whitelisted : Filter() {
    override fun filter(name: String?, viewer: OfflinePlayer?): Boolean {
        val player = getOffline(name)
        return player != null && player.isWhitelisted
    }
}
