package io.github.tanguygab.playerlistexpansion.filters;

import com.viaversion.viaversion.api.Via;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Version extends Filter {

    private final List<Integer> versions = new ArrayList<>();
    private final Map<Integer, Integer> ranges = new HashMap<>();

    public Version(String arg) {
        for (String version : arg.split("\\+")) {
            try {
                if (version.contains("-")) {
                    String[] range = version.split("-");
                    ranges.put(Integer.parseInt(range[0]),Integer.parseInt(range[1]));
                    continue;
                }
                versions.add(Integer.parseInt(version));
            } catch (Exception ignored) {}
        }
    }

    @Override
    public boolean filter(String name, OfflinePlayer viewer) {
        Player player = getOnline(name);
        if (player == null) return false;
        int version = Via.getAPI().getPlayerVersion(player.getUniqueId());
        return versions.contains(version) || ranges.keySet().stream().anyMatch(ver->version >= ver && version <= ranges.get(ver));
    }
}
