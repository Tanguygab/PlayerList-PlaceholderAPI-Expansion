package io.github.tanguygab.playerlistexpansion.filters;

import org.bukkit.OfflinePlayer;

import java.util.stream.Stream;

public class CanSee extends Filter {

    @Override
    public Stream<OfflinePlayer> filter(Stream<OfflinePlayer> stream, OfflinePlayer viewer) {
        return stream.filter(p->isOnline(p,viewer) && p.getPlayer().canSee(viewer.getPlayer()));
    }

}
