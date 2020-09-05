package io.github.tanguygab.playerlistexpansion.subtype;

import java.util.ArrayList;
import java.util.Collection;
import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.OfflinePlayer;

public class Placeholder extends SubType {

	public Placeholder(boolean countSelf, String output, String subtypevalue) {
		super(countSelf, output, subtypevalue);
	}

	@Override
	public String getText(OfflinePlayer player, Collection<? extends OfflinePlayer> playerList) {
		ArrayList<String> playersPlaceholder = new ArrayList<>();
		for (OfflinePlayer p : playerList) {
			String[] placeholder = subTypeValue.split("\\|\\|");
			if (countSelf || !p.getName().equals(player.getName())) {
				if (PlaceholderAPI.setPlaceholders(p, "%"+placeholder[0]+"%").equals(PlaceholderAPI.setBracketPlaceholders(player, placeholder[1]))) {
					playersPlaceholder.add(p.getName());
				}
			}
		}
		return format(player, playersPlaceholder);
	}
}