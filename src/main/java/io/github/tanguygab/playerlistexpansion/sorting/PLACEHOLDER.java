package io.github.tanguygab.playerlistexpansion.sorting;

import io.github.tanguygab.playerlistexpansion.PlayerListExpansion;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import java.util.LinkedHashMap;
import java.util.Map;

public class PLACEHOLDER extends SortingType {

    private final Map<String,Integer> sortingMap = new LinkedHashMap<>();

    public PLACEHOLDER(String arg) {
        super(arg.substring(arg.indexOf(":")));
        arg = arg.substring(arg.indexOf(":")+1);
        if (arg.isEmpty()) {
            return;
        }
        String[] outputs = arg.split(PlayerListExpansion.get().argumentSeparator);
        int index = 1;
        for (String element : outputs)
            for (String element0 : element.split("\\|"))
                sortingMap.put(ChatColor.translateAlternateColorCodes('&',element0.trim().toLowerCase()), index++);
    }

    @Override
    public String getSortingString(String name, OfflinePlayer viewer) {
        String output = parse(name,viewer);
        int position = sortingMap.getOrDefault(output,sortingMap.size()+1);
        return String.valueOf(position);
    }
}
