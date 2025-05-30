package io.github.tanguygab.playerlistexpansion.filters

import org.bukkit.OfflinePlayer

class Nearby(arg: String) : Filter() {
    private val distance = arg.toDoubleOrNull() ?: 0.0

    override fun filter(name: String?, viewer: OfflinePlayer?): Boolean {
        val player = getOnline(name)
        val viewerPlayer = viewer?.player
        return player != null && viewerPlayer != null
                && player.world === viewerPlayer.world
                && viewerPlayer.location.distance(player.location) < distance
    }
}
