package io.github.tanguygab.playerlistexpansion.filters;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class CanSee extends Filter {

    @Override
    public boolean filter(String name, OfflinePlayer viewer) {
        Player player = getOnline(name);
        Player viewerPlayer = viewer.getPlayer();
        return player != null && viewerPlayer != null && viewerPlayer.canSee(player);
    }

}
