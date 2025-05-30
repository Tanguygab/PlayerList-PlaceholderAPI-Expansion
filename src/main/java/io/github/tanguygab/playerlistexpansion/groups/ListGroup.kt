package io.github.tanguygab.playerlistexpansion.groups

import io.github.tanguygab.playerlistexpansion.PlayerListExpansion
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.OfflinePlayer
import java.util.Collections

class ListGroup(private val lists: MutableList<GroupedList>, private val gap: Int) {

    fun getText(viewer: OfflinePlayer?, arguments: String): String? {
        val args = arguments.split("_")
        val format = PlayerListExpansion.get().getFormat(arguments, args)
        val arg = args[0]

        val slots = getList(viewer, arg == "amount", format)

        val pos = arg.toIntOrNull()
        return when {
            arg == "amount" -> slots.size.toString()
            pos === null -> null
            pos >= 0 && pos < slots.size -> slots[pos]
            else -> ""
        }
    }

    private fun getList(viewer: OfflinePlayer?, amount: Boolean, format: String): List<String> {
        val slots = ArrayList<String>()

        for (list in lists) {
            val players = list.list.getList(viewer, format)
            if (players.isEmpty()) continue
            if (amount) {
                slots.addAll(players)
                continue
            }

            if (lists.indexOf(list) != 0 && gap != 0) {
                slots.addAll(Collections.nCopies(gap, ""))
            }
            slots.add(setPlaceholders(viewer, list.title, "%amount%", players.size))
            if (list.max > 0 && players.size > list.max) {
                for (i in 0..<list.max) {
                    slots.add(players[i])
                }
                if (list.remaining !== null)
                    slots.add(setPlaceholders(viewer, list.remaining, "%remaining%", players.size - list.max))
                continue
            }
            slots.addAll(players)
        }

        return slots
    }

    private fun setPlaceholders(viewer: OfflinePlayer?, string: String, placeholder: String, amount: Int): String {
        return PlaceholderAPI.setPlaceholders(viewer, string.replace(placeholder, amount.toString() + ""))
    }
}
