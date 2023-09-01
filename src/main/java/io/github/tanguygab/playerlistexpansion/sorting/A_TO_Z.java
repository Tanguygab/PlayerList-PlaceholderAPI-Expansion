package io.github.tanguygab.playerlistexpansion.sorting;

import org.bukkit.OfflinePlayer;

public class A_TO_Z extends SortingType {



    @Override
    public String getSortingString(String name, OfflinePlayer viewer) {
        return name;
    }
}
