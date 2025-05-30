package io.github.tanguygab.playerlistexpansion.sorting

import org.bukkit.OfflinePlayer

class LOW_TO_HIGH(arg: String?) : SortingType(arg) {
    override fun getSortingString(name: String, viewer: OfflinePlayer?): String {
        val string = parse(name, viewer)
        val num: Double = DEFAULT_NUMBER + parseDouble(string)
        return num.toString()
    }
}
