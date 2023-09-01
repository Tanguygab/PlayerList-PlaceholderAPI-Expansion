package io.github.tanguygab.playerlistexpansion.filters;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class World extends Filter {

    private final List<String> worlds;

    public World(String arg) {
        worlds = Arrays.asList(split(arg));
    }

    @Override
    public boolean filter(String name, OfflinePlayer viewer) {
        Player player = getOnline(name);
        return player != null && worlds.contains(player.getWorld().getName());
    }
}
