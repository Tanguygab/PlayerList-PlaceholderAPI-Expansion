package io.github.tanguygab.playerlistexpansion.filters;

import org.bukkit.OfflinePlayer;

import java.util.stream.Stream;

public class Permission extends Filter {

    private final String permission;

    public Permission(String permission) {
        this.permission = permission;
    }

    @Override
    public Stream<OfflinePlayer> filter(Stream<OfflinePlayer> stream, OfflinePlayer viewer) {
        return stream.filter(p->isOnline(p) && p.getPlayer().hasPermission(permission));
    }

}
