package io.github.tanguygab.playerlistexpansion.sorting;

import org.bukkit.OfflinePlayer;

public class LOW_TO_HIGH extends SortingType {

    public LOW_TO_HIGH(String arg) {
        super(arg);
    }

    @Override
    public String getSortingString(String name, OfflinePlayer viewer) {
        String string = parse(name,viewer);
        double num = DEFAULT_NUMBER + parseDouble(string);
        return String.valueOf(num);
    }
}
