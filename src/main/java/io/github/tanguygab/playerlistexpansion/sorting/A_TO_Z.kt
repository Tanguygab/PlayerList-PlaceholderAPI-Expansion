package io.github.tanguygab.playerlistexpansion.sorting

import org.bukkit.OfflinePlayer

class A_TO_Z(arg: String?) : SortingType(arg) {
    override fun getSortingString(name: String, viewer: OfflinePlayer?): String {
        return parse(name, viewer)
    }
}
