package io.github.tanguygab.playerlistexpansion.groups;

import io.github.tanguygab.playerlistexpansion.PlayerListExpansion;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListGroup {

    private final List<GroupedList> lists;
    private final int gap;

    public ListGroup(List<GroupedList> lists, int gap) {
        this.lists = lists;
        this.gap = gap;
    }

    public String getText(OfflinePlayer viewer, String arguments) {
        String[] args = arguments.split("_");
        String format = PlayerListExpansion.get().getFormat(arguments, args);
        String arg = args[0];

        List<String> slots = getList(viewer, arg.equals("amount"), format);

        if (arg.equals("amount")) return slots.size()+"";

        int pos;
        try {pos = Integer.parseInt(arg);}
        catch (Exception e) {return null;}
        return pos >= 0 && pos < slots.size() ? slots.get(pos) : "";
    }

    private List<String> getList(OfflinePlayer viewer, boolean amount, String format) {
        List<String> slots = new ArrayList<>();

        for (GroupedList list : lists) {
            List<String> players = list.getList().getList(viewer, format);
            if (players.isEmpty()) continue;
            if (amount) {
                slots.addAll(players);
                continue;
            }

            if (lists.indexOf(list) != 0 && gap != 0) {
                slots.addAll(Collections.nCopies(gap,""));
            }

            slots.add(setPlaceholders(viewer, list.getTitle(), "%amount%",players.size()));
            if (list.getMax() > 0 && players.size() > list.getMax()) {
                for (int i = 0; i < list.getMax(); i++) {
                    slots.add(players.get(i));
                }
                slots.add(setPlaceholders(viewer, list.getRemaining(),"%remaining%", players.size() - list.getMax()));
            } else slots.addAll(players);
        }

        return slots;
    }

    private String setPlaceholders(OfflinePlayer viewer, String string, String placeholder, int amount) {
        return PlaceholderAPI.setPlaceholders(viewer, string.replace(placeholder,amount+""));
    }

}
