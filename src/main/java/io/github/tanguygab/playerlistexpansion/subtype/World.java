package io.github.tanguygab.playerlistexpansion.subtype;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class World extends SubType {

	public World(boolean countSelf, String output, String subtypevalue) {
		super(countSelf, output, subtypevalue);
	}

	@Override
	public String getText(OfflinePlayer player, Collection<? extends OfflinePlayer> playerList) {
		ArrayList<String> playersWorld = new ArrayList<>();
		for (OfflinePlayer off : playerList) {
			if (!(off instanceof Player)) continue;
			Player p = (Player) off;
			if (countSelf || !p.getName().equals(player.getName())) {
				if (("+" + subTypeValue + "+").contains("+" + p.getWorld().getName() + "+")) {
					playersWorld.add(p.getName());
				}
			}
		}
		return format(player, playersWorld);
	}
}