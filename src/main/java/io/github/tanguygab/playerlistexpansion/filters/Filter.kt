package io.github.tanguygab.playerlistexpansion.filters

import io.github.tanguygab.playerlistexpansion.PlayerListExpansion
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*
import java.util.function.Function

abstract class Filter {
    var isInverted: Boolean = false
        private set

    protected fun split(arg: String): List<String> {
        return arg.split(PlayerListExpansion.get().argumentSeparator)
    }

    protected fun getOffline(name: String?): OfflinePlayer? {
        return if (name === null) null else Bukkit.getServer().getOfflinePlayerIfCached(name)
    }

    protected fun getOnline(name: String?): Player? {
        return if (name === null) null else Bukkit.getServer().getPlayerExact(name)
    }

    abstract fun filter(name: String?, viewer: OfflinePlayer?): Boolean

    companion object {
        private val filters = HashMap<String, Function<String?, Filter>>().apply {
            put("BANNED") { Banned() }
            put("CANSEE") { CanSee() }
            put("GAMEMODE") { GameMode(it) }
            put("NEARBY") { Nearby(it!!) }
            put("PERMISSION") { Permission(it!!) }
            put("PLACEHOLDER") { Placeholder(it!!) }
            if (Bukkit.getServer().pluginManager.isPluginEnabled("ViaVersion"))
                put("VERSION") { Version(it!!) }
            put("WHITELISTED") { Whitelisted() }
            put("WORLD") { World(it!!) }
        }

        fun find(string: String): Filter? {
            val args = string.split(":", limit = 2)

            val inverted = args[0][0] == '!'
            val filter = args[0].uppercase().substring(if (inverted) 1 else 0)
            val arg = if (args.size > 1) args[1] else null

            if (filters.containsKey(filter)) {
                val f = filters[filter]!!.apply(arg)
                f.isInverted = inverted
                return f
            }
            return null
        }
    }
}
