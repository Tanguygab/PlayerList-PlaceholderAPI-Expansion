package io.github.tanguygab.playerlistexpansion.filters;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;

public class Placeholder extends Filter {

    private final String leftSide;
    private final String rightSide;

    public Placeholder(String arg) {
        String[] sides = split(arg);
        leftSide = sides[0];
        rightSide = sides[1];
    }

    @Override
    public boolean filter(String name, OfflinePlayer viewer) {
        return PlaceholderAPI.setPlaceholders(getOffline(name), leftSide).equals(PlaceholderAPI.setPlaceholders(viewer, rightSide));
    }
}
