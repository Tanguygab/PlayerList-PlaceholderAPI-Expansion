package io.github.tanguygab.playerlistexpansion.filters

import com.viaversion.viaversion.api.Via
import org.bukkit.OfflinePlayer

class Version(arg: String) : Filter() {
    private val versions = ArrayList<Int?>()
    private val ranges = HashMap<Int?, Int?>()

    init {
        for (version in arg.split("+")) {
            try {
                if (version.contains("-")) {
                    val range = version.split("-")
                    ranges.put(range[0].toInt(), range[1].toInt())
                    continue
                }
                versions.add(version.toInt())
            } catch (_: Exception) {}
        }
    }

    override fun filter(name: String?, viewer: OfflinePlayer?): Boolean {
        val player = getOnline(name)
        if (player == null) return false
        val version = Via.getAPI().getPlayerVersion(player.uniqueId)
        return versions.contains(version) || ranges.keys.stream()
            .anyMatch { ver: Int? -> version >= ver!! && version <= ranges[ver]!! }
    }
}
