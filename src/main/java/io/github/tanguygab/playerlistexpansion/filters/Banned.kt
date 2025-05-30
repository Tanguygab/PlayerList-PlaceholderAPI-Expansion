package io.github.tanguygab.playerlistexpansion.filters;

import org.bukkit.OfflinePlayer;

public class Banned extends Filter {

    @Override
    public boolean filter(String name, OfflinePlayer viewer) {
        OfflinePlayer player = getOffline(name);
        return player != null && player.isBanned();
    }

}
