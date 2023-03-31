package io.github.tanguygab.playerlistexpansion.filters;

import io.github.tanguygab.playerlistexpansion.PlayerListExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public abstract class Filter {

    private static final Map<String,Function<String,Filter>> filters = new HashMap<String,Function<String,Filter>>() {{
        put("banned",arg->new Banned());
        put("cansee",arg->new CanSee());
        put("gamemode", GameMode::new);
        put("nearby",Nearby::new);
        put("permission",Permission::new);
        put("placeholder",Placeholder::new);
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("ViaVersion"))
            put("version",Version::new);
        put("whitelisted",arg->new Whitelisted());
        put("world",World::new);
    }};
    public static Filter find(String filter, String arg) {
        return filters.containsKey(filter.toLowerCase()) ? filters.get(filter.toLowerCase()).apply(arg) : null;
    }

    protected boolean isOnline(OfflinePlayer... players) {
        for (OfflinePlayer p : players)
            if (p.getPlayer() == null)
                return false;
        return true;
    }
    protected String[] split(String arg) {
        return arg.split(Pattern.quote(PlayerListExpansion.get().argumentSeparator));
    }

    public abstract Stream<OfflinePlayer> filter(Stream<OfflinePlayer> stream, OfflinePlayer viewer);

}