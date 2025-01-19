package io.github.tanguygab.playerlistexpansion;

import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.function.Function;

public class CachedList {

    private long lastUpdate;
    private final Function<OfflinePlayer, List<String>> fun;
    private List<String> list;

    public CachedList(Function<OfflinePlayer, List<String>> fun) {
        this.fun = fun;
    }

    public List<String> getList(OfflinePlayer player) {
        long now = System.currentTimeMillis();
        System.out.println(now - lastUpdate > PlayerListExpansion.get().updateCooldown);
        if (now - lastUpdate > PlayerListExpansion.get().updateCooldown) {
            list = fun.apply(player);
            lastUpdate = now;
        }
        return list;
    }
}
