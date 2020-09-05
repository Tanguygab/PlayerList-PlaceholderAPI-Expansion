package io.github.tanguygab.playerlistexpansion.subtype;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.OfflinePlayer;

public class Banned extends SubType {

	public Banned(boolean countSelf, String output, String subtypevalue) {
		super(countSelf, output, subtypevalue);
	}

	@Override
	public String getText(OfflinePlayer player, Collection<? extends OfflinePlayer> playerList) {
		ArrayList<String> playersBanned = new ArrayList<>();
		for (OfflinePlayer p : playerList) {
			if (p.isBanned()) {
				playersBanned.add(p.getName());
			}
		}
		return format(player, playersBanned);
	}
}