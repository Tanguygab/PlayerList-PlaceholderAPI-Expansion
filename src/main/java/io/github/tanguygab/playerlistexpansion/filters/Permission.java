package io.github.tanguygab.playerlistexpansion.filters;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class Permission extends Filter {

    private final List<String> permissions;

    public Permission(String arg) {
        this.permissions = Arrays.asList(split(arg));
    }

    @Override
    public boolean filter(String name, OfflinePlayer viewer) {
        Player player = getOnline(name);
        return player != null && permissions.stream().anyMatch(player::hasPermission);
    }
}
