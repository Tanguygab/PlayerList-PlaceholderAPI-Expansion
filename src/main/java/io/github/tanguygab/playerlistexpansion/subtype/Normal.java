package io.github.tanguygab.playerlistexpansion.subtype;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.OfflinePlayer;

public class Normal extends SubType {

	public Normal(boolean countSelf, String output, String subtypevalue) {
		super(countSelf, output, subtypevalue);
	}

	@Override
	public String getText(OfflinePlayer player, Collection<? extends OfflinePlayer> playerList) {
		ArrayList<String> players = new ArrayList<>();
		for (OfflinePlayer p : playerList) {
			if (countSelf || !p.getName().equals(player.getName())) {
				players.add(p.getName());
			}
		}
		return format(player, players);
	}
}