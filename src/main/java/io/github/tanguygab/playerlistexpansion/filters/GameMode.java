package io.github.tanguygab.playerlistexpansion.filters;

import org.bukkit.OfflinePlayer;

import java.util.stream.Stream;

public class GameMode extends Filter {

    private final org.bukkit.GameMode mode;

    public GameMode(String arg) {
        org.bukkit.GameMode mode;
        try {mode = org.bukkit.GameMode.valueOf(arg);}
        catch (Exception e) {mode = org.bukkit.GameMode.SURVIVAL;}
        this.mode = mode;
    }

    @Override
    public Stream<OfflinePlayer> filter(Stream<OfflinePlayer> stream, OfflinePlayer viewer) {
        return stream.filter(p->isOnline(p) && p.getPlayer().getGameMode() == mode);
    }

}
