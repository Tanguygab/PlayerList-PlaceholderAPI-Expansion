package io.github.tanguygab.playerlistexpansion.filters;

import com.viaversion.viaversion.api.Via;
import org.bukkit.OfflinePlayer;

import java.util.stream.Stream;

public class Version extends Filter {

    private final int version;

    public Version(String arg) {
        int version;
        try {version = Integer.parseInt(arg);}
        catch (Exception e) {version = Via.getAPI().getServerVersion().lowestSupportedVersion();}
        this.version = version;
    }

    @Override
    public Stream<OfflinePlayer> filter(Stream<OfflinePlayer> stream, OfflinePlayer viewer) {
        return stream.filter(p->isOnline(p) && Via.getAPI().getPlayerVersion(p.getUniqueId()) == version);
    }

}
