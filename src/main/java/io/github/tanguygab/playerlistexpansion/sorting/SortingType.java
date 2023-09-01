package io.github.tanguygab.playerlistexpansion.sorting;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class SortingType {

    @SuppressWarnings("all")
    private static final Map<String, Function<String, SortingType>> types = new HashMap<String,Function<String,SortingType>>() {{
        put("A_TO_Z",A_TO_Z::new);
        put("Z_TO_A",Z_TO_A::new);
        put("LOW_TO_HIGH",LOW_TO_HIGH::new);
        put("HIGH_TO_LOW",HIGH_TO_LOW::new);
        put("PLACEHOLDER",PLACEHOLDER::new);
    }};

    public static SortingType find(String string) {
        String[] args = string.split(":");
        String type = args[0].toUpperCase();
        String arg = args.length > 1 ? args[1] : null;
        type = type.toUpperCase();
        return types.containsKey(type) ? types.get(type).apply(arg) : null;
    }

    private final String placeholder;

    protected SortingType(String placeholder) {
        this.placeholder = placeholder;
    }

    protected String parse(String name, OfflinePlayer viewer) {
        if (placeholder == null) return name;
        if (!placeholder.contains("%")) return placeholder;

        String output = placeholder;
        Player onlinePlayer = Bukkit.getPlayerExact(name);
        if (onlinePlayer != null && viewer.getPlayer() != null)
            output = PlaceholderAPI.setRelationalPlaceholders(viewer.getPlayer(),onlinePlayer,output);

        output = PlaceholderAPI.setPlaceholders(onlinePlayer == null ? getOffline(name) : onlinePlayer, output);
        return output;
    }

    protected OfflinePlayer getOffline(String name) {
        for (OfflinePlayer player : Bukkit.getServer().getOfflinePlayers())
            if (name.equals(player.getName()))
                return player;
        return null;
    }

    protected double parseDouble(String string) {
        try {return Double.parseDouble(string);}
        catch (Exception e) {return 0;}
    }

    public abstract String getSortingString(String name, OfflinePlayer viewer);

}
