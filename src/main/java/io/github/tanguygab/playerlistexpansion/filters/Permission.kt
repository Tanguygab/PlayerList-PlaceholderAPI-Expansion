package io.github.tanguygab.playerlistexpansion.filters

import org.bukkit.OfflinePlayer

class Permission(arg: String) : Filter() {
    private val permissions = split(arg)

    override fun filter(name: String?, viewer: OfflinePlayer?): Boolean {
        val player = getOnline(name)
        return player != null && permissions.any { player.hasPermission(it) }
    }
}
