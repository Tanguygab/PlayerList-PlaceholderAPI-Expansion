package io.github.tanguygab.playerlistexpansion.sorting;

import org.bukkit.OfflinePlayer;

public class HIGH_TO_LOW extends SortingType {

    public HIGH_TO_LOW(String arg) {
        super(arg);
    }

    @Override
    public String getSortingString(String name, OfflinePlayer viewer) {
        String string = parse(name,viewer);
        double num = DEFAULT_NUMBER - parseDouble(string);
        return String.valueOf(num);
    }
}
