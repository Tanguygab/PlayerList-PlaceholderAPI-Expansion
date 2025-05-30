package io.github.tanguygab.playerlistexpansion.filters

import org.bukkit.GameMode
import org.bukkit.OfflinePlayer

class GameMode(arg: String?) : Filter() {
    private val mode = GameMode.entries.find { arg == it.name } ?: GameMode.SURVIVAL

    override fun filter(name: String?, viewer: OfflinePlayer?): Boolean {
        val player = getOnline(name)
        return player != null && player.gameMode == mode
    }
}
