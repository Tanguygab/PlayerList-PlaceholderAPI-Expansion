package io.github.tanguygab.playerlistexpansion.subtype;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.OfflinePlayer;

public class WhiteListed extends SubType {

	public WhiteListed(boolean countSelf, String output, String subtypevalue) {
		super(countSelf, output, subtypevalue);
	}

	@Override
	public String getText(OfflinePlayer player, Collection<? extends OfflinePlayer> playerList) {
		ArrayList<String> playersWhitelisted = new ArrayList<>();
		for (OfflinePlayer p : playerList) {
			if (countSelf || !p.getName().equals(player.getName())) {
				if (p.isWhitelisted()) {
					playersWhitelisted.add(p.getName());
				}
			}
		}
		return format(player, playersWhitelisted);
	}
}