package io.github.tanguygab.playerlistexpansion.sorting;

import org.bukkit.OfflinePlayer;

public class A_TO_Z extends SortingType {

    public A_TO_Z(String arg) {
        super(arg);
    }

    @Override
    public String getSortingString(String name, OfflinePlayer viewer) {
        return parse(name,viewer);
    }
}
