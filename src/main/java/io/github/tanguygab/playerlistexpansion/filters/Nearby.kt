package io.github.tanguygab.playerlistexpansion.filters;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Nearby extends Filter {

    private final double distance;

    public Nearby(String arg) {
        double distance;
        try {distance = Double.parseDouble(arg);}
        catch (Exception e) {distance = 0;}
        this.distance = distance;
    }

    @Override
    public boolean filter(String name, OfflinePlayer viewer) {
        Player player = getOnline(name);
        Player viewerPlayer = viewer.getPlayer();
        return player != null
                && viewerPlayer != null
                && player.getWorld() == viewerPlayer.getWorld()
                && viewerPlayer.getLocation().distance(player.getLocation()) < distance;
    }
}
