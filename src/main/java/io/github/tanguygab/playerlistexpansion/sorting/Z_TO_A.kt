package io.github.tanguygab.playerlistexpansion.sorting

import org.bukkit.OfflinePlayer

class Z_TO_A(arg: String?) : SortingType(arg) {
    override fun getSortingString(name: String, viewer: OfflinePlayer?): String {
        val chars = parse(name, viewer).toCharArray()
        for (i in chars.indices) {
            val c = chars[i]
            if (c.code >= 65 && c.code <= 90) chars[i] = (155 - c.code).toChar()
            if (c.code >= 97 && c.code <= 122) chars[i] = (219 - c.code).toChar()
        }
        return String(chars)
    }
}
