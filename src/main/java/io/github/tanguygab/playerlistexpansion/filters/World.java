package io.github.tanguygab.playerlistexpansion.filters;

import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class World extends Filter {

    private final List<String> worlds;

    public World(String arg) {
        worlds = Arrays.asList(split(arg));
    }

    @Override
    public Stream<OfflinePlayer> filter(Stream<OfflinePlayer> stream, OfflinePlayer viewer) {
        return stream.filter(p->isOnline(p) && worlds.contains(p.getPlayer().getWorld().getName()));
    }

}
