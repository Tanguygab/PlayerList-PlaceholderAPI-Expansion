package io.github.tanguygab.playerlistexpansion.filters;

import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Permission extends Filter {

    private final List<String> permissions;

    public Permission(String arg) {
        this.permissions = Arrays.asList(split(arg));
    }

    @Override
    public Stream<OfflinePlayer> filter(Stream<OfflinePlayer> stream, OfflinePlayer viewer) {
        return stream.filter(p->isOnline(p) && permissions.stream().anyMatch(perm->p.getPlayer().hasPermission(perm)));
    }

}
