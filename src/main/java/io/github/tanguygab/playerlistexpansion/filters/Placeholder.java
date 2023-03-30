package io.github.tanguygab.playerlistexpansion.filters;

import io.github.tanguygab.playerlistexpansion.PlayerListExpansion;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;

import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Placeholder extends Filter {

    private final String leftside;
    private final String rightside;

    public Placeholder(String arg) {
        String[] sides = arg.split(Pattern.quote(PlayerListExpansion.get().argumentSeparator));
        leftside = sides[0];
        rightside = sides[1];
    }

    @Override
    public Stream<OfflinePlayer> filter(Stream<OfflinePlayer> stream, OfflinePlayer viewer) {
        return stream.filter(p->PlaceholderAPI.setPlaceholders(p,leftside).equals(PlaceholderAPI.setPlaceholders(viewer,rightside)));
    }

}
