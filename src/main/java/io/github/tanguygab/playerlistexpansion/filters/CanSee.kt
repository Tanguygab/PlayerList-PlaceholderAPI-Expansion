package io.github.tanguygab.playerlistexpansion.filters

import org.bukkit.OfflinePlayer

class CanSee : Filter() {
    override fun filter(name: String?, viewer: OfflinePlayer?): Boolean {
        val player = getOnline(name)
        val viewerPlayer = viewer?.player
        return player != null && viewerPlayer != null && viewerPlayer.canSee(player)
    }
}
