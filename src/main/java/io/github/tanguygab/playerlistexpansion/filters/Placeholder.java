package io.github.tanguygab.playerlistexpansion.filters;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;

public class Placeholder extends Filter {

    private final List<String[]> placeholders = new ArrayList<>();

    public Placeholder(String arg) {
        for (String str : split(arg))
            placeholders.add(str.split("="));
    }

    @Override
    public boolean filter(String name, OfflinePlayer viewer) {
        for (String[] arr : placeholders)
            if (parse(name,viewer,arr))
                return true;
        return false;
    }

    private boolean parse(String name, OfflinePlayer viewer, String[] arr) {
        return PlaceholderAPI.setPlaceholders(getOffline(name), arr[0]).equals(PlaceholderAPI.setPlaceholders(viewer, arr[1]));
    }
}
