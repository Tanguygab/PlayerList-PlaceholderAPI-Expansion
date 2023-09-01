package io.github.tanguygab.playerlistexpansion.filters;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class GameMode extends Filter {

    private final org.bukkit.GameMode mode;

    public GameMode(String arg) {
        org.bukkit.GameMode mode;
        try {mode = org.bukkit.GameMode.valueOf(arg);}
        catch (Exception e) {mode = org.bukkit.GameMode.SURVIVAL;}
        this.mode = mode;
    }

    @Override
    public boolean filter(String name, OfflinePlayer viewer) {
        Player player = getOnline(name);
        return player != null && player.getGameMode() == mode;
    }
}
