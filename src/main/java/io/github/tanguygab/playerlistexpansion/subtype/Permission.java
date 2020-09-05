package io.github.tanguygab.playerlistexpansion.subtype;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Permission extends SubType {

	public Permission(boolean countSelf, String output, String subtypevalue) {
		super(countSelf, output, subtypevalue);
	}

	@Override
	public String getText(OfflinePlayer player, Collection<? extends OfflinePlayer> playerList) {
		ArrayList<String> playersPerm = new ArrayList<>();
		for (OfflinePlayer off : playerList) {
			if (!(off instanceof Player)) continue;
			Player p = (Player) off;
			if (countSelf || !p.getName().equals(player.getName())) {
				for (String perm : subTypeValue.split("\\+"))
					if (!playersPerm.contains(p.getName()) && p.hasPermission(perm)) {
						playersPerm.add(p.getName());
					}
			}
		}
		return format(player, playersPerm);
	}
}