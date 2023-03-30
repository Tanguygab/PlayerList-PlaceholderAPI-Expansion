package io.github.tanguygab.playerlistexpansion.filters;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.stream.Stream;

public class World extends Filter {

    private final org.bukkit.World world;

    public World(String arg) {
        org.bukkit.World world;
        try {world = Bukkit.getServer().getWorld(arg);}
        catch (Exception e) {world = Bukkit.getServer().getWorlds().get(0);}
        this.world = world;
    }

    @Override
    public Stream<OfflinePlayer> filter(Stream<OfflinePlayer> stream, OfflinePlayer viewer) {
        return stream.filter(p->isOnline(p) && p.getPlayer().getWorld() == world);
    }

}
