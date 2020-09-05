package io.github.tanguygab.playerlistexpansion.subtype;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Nearby extends SubType {

	public Nearby(boolean countSelf, String output, String subtypevalue) {
		super(countSelf, output, subtypevalue);
	}

	@Override
	public String getText(OfflinePlayer player, Collection<? extends OfflinePlayer> playerList) {
		Integer zone = (int) Math.pow(Integer.parseInt(subTypeValue), 2);
		ArrayList<String> playersNearby = new ArrayList<>();
		for (OfflinePlayer off : playerList) {
			if (!(off instanceof Player)) continue;
			Player p = (Player) off;
			if (countSelf || !p.getName().equals(player.getName())) {
				if (p.getWorld().equals(player.getPlayer().getWorld()) && player.getPlayer().getLocation().distanceSquared(p.getLocation()) < zone) {
					playersNearby.add(p.getName());
				}
			}
		}
		return format(player, playersNearby);
	}
}