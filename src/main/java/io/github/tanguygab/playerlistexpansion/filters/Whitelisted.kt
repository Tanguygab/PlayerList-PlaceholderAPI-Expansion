package io.github.tanguygab.playerlistexpansion.filters;

import org.bukkit.OfflinePlayer;

public class Whitelisted extends Filter {

    @Override
    public boolean filter(String name, OfflinePlayer viewer) {
        OfflinePlayer player = getOffline(name);
        return player != null && player.isWhitelisted();
    }
}
