package io.github.tanguygab.playerlistexpansion.subtype;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class CanSee extends SubType {

	public CanSee(boolean countSelf, String output, String subtypevalue) {
		super(countSelf, output, subtypevalue);
	}

	@Override
	public String getText(OfflinePlayer player, Collection<? extends OfflinePlayer> playerList) {
		ArrayList<String> playersCanSee = new ArrayList<>();
		for (OfflinePlayer off : playerList) {
			if (!(off instanceof Player)) continue;
			Player p = (Player) off;
			if (!countSelf) {
				if (!player.getPlayer().canSee(p) && !p.getName().equals(player.getName())) {
					playersCanSee.add(p.getName());
				}
			} else {
				if (player.getPlayer().canSee(p) && !p.getName().equals(player.getName())) {
					playersCanSee.add(p.getName());
				}
			}
		}
		return format(player, playersCanSee);
	}
}