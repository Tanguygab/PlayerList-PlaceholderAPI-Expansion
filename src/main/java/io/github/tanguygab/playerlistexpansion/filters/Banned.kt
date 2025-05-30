package io.github.tanguygab.playerlistexpansion.filters

import org.bukkit.OfflinePlayer

class Banned : Filter() {
    override fun filter(name: String?, viewer: OfflinePlayer?): Boolean {
        val player = getOffline(name)
        return player != null && player.isBanned
    }
}
