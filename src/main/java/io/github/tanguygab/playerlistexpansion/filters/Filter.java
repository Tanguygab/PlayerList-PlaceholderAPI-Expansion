package io.github.tanguygab.playerlistexpansion.filters;

import io.github.tanguygab.playerlistexpansion.PlayerListExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class Filter {

    @SuppressWarnings("all")
    private static final Map<String,Function<String,Filter>> filters = new HashMap<String,Function<String,Filter>>() {{
        put("BANNED",arg->new Banned());
        put("CANSEE",arg->new CanSee());
        put("GAMEMODE",GameMode::new);
        put("NEARBY",Nearby::new);
        put("PERMISSION",Permission::new);
        put("PLACEHOLDER",Placeholder::new);
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("ViaVersion"))
            put("VERSION",Version::new);
        put("WHITELISTED",arg->new Whitelisted());
        put("WORLD",World::new);
    }};
    public static Filter find(String string) {
        int index = string.indexOf(":");
        String filter = index == -1 ? string : string.substring(0,index);
        filter = filter.toUpperCase();
        String arg = index == -1 ? null : string.substring(index+1);
        return filters.containsKey(filter) ? filters.get(filter).apply(arg) : null;
    }

    protected String[] split(String arg) {
        return arg.split(PlayerListExpansion.get().argumentSeparator);
    }

    protected OfflinePlayer getOffline(String name) {
        for (OfflinePlayer player : Bukkit.getServer().getOfflinePlayers())
            if (name.equals(player.getName()))
                return player;
        return null;
    }
    protected Player getOnline(String name) {
        return Bukkit.getServer().getPlayerExact(name);
    }

    public abstract boolean filter(String name, OfflinePlayer viewer);

}
