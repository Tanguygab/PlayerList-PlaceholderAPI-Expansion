package io.github.tanguygab.playerlistexpansion

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import java.util.concurrent.Callable

enum class ListType(
    var callable: Callable<Collection<OfflinePlayer>>?
) {
    ONLINE({ Bukkit.getServer().onlinePlayers }),
    OFFLINE({ Bukkit.getServer().offlinePlayers.filter { p: OfflinePlayer -> !p.isOnline }.toList() }),
    ALL({ Bukkit.getServer().offlinePlayers.toList() }),
    CUSTOM(null);

    fun getList(): Collection<OfflinePlayer> {
        return callable!!.call()
    }

    companion object {
        fun find(str: String?): ListType? {
            if (str == null) return null
            return entries.find { it.name.equals(str, true) }
        }
    }
}
