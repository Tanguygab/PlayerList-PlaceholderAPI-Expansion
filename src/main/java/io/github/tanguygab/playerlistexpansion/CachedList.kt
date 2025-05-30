package io.github.tanguygab.playerlistexpansion

import org.bukkit.OfflinePlayer
import java.util.function.BiFunction

class CachedList(private val function: BiFunction<OfflinePlayer?, String, List<String>>) {
    private var lastUpdate: Long = 0
    private var list = listOf<String>()

    fun getList(player: OfflinePlayer?, format: String): List<String> {
        val now = System.currentTimeMillis()
        if (now - lastUpdate > PlayerListExpansion.get().updateCooldown) {
            list = function.apply(player, format)
            lastUpdate = now
        }
        return list
    }
}
