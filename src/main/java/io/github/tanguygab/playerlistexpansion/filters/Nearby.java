package io.github.tanguygab.playerlistexpansion.filters;

import org.bukkit.OfflinePlayer;

import java.util.stream.Stream;

public class Nearby extends Filter {

    private final double distance;

    public Nearby(String arg) {
        double distance;
        try {distance = Double.parseDouble(arg);}
        catch (Exception e) {distance = 0;}
        this.distance = Math.pow(distance, 2);
    }

    @Override
    public Stream<OfflinePlayer> filter(Stream<OfflinePlayer> stream, OfflinePlayer viewer) {
        return stream.filter(p->isOnline(p,viewer)
                && viewer.getPlayer().getWorld().equals(p.getPlayer().getWorld())
                && viewer.getPlayer().getLocation().distanceSquared(p.getPlayer().getLocation()) < distance);
    }

}
