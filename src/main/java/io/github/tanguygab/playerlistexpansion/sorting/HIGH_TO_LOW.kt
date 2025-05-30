package io.github.tanguygab.playerlistexpansion.sorting

import org.bukkit.OfflinePlayer

class HIGH_TO_LOW(arg: String?) : SortingType(arg) {
    override fun getSortingString(name: String, viewer: OfflinePlayer?): String {
        val string = parse(name, viewer)
        val num: Double = DEFAULT_NUMBER - parseDouble(string)
        return num.toString()
    }
}
