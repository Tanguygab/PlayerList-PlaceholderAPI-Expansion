package io.github.tanguygab.playerlistexpansion.filters

import org.bukkit.OfflinePlayer

class World(arg: String) : Filter() {
    private val worlds = split(arg)

    override fun filter(name: String?, viewer: OfflinePlayer?): Boolean {
        val player = getOnline(name)
        return player != null && worlds.contains(player.world.name)
    }
}
