package io.github.tanguygab.playerlistexpansion;

import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.function.BiFunction;

public class CachedList {

    private long lastUpdate;
    private final BiFunction<OfflinePlayer, String, List<String>> fun;
    private List<String> list;

    public CachedList(BiFunction<OfflinePlayer, String, List<String>> fun) {
        this.fun = fun;
    }

    public List<String> getList(OfflinePlayer player, String format) {
        long now = System.currentTimeMillis();
        if (now - lastUpdate > PlayerListExpansion.get().updateCooldown) {
            list = fun.apply(player, format);
            lastUpdate = now;
        }
        return list;
    }
}
