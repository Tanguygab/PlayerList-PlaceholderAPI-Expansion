package io.github.tanguygab.playerlistexpansion.subtype;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.OfflinePlayer;

public class Gamemode extends SubType {

    public Gamemode(boolean countSelf, String output, String subtypevalue) {
        super(countSelf, output, subtypevalue);
    }

    @Override
    public String getText(OfflinePlayer player, Collection<? extends OfflinePlayer> playerList) {
        ArrayList<String> playersGamemode = new ArrayList<>();
        for (OfflinePlayer p : playerList) {
            if ((countSelf || !p.getName().equals(player.getName())) && p.getPlayer().getGameMode().toString().equals(subTypeValue)) {
                playersGamemode.add(p.getName());
            }
        }
        return format(player, playersGamemode);
    }
}