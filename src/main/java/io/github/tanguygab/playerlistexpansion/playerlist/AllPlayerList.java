package io.github.tanguygab.playerlistexpansion.playerlist;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import io.github.tanguygab.playerlistexpansion.subtype.SubType;

public class AllPlayerList extends PlayerList {

	private List<OfflinePlayer> allPlayers = Arrays.asList(Bukkit.getOfflinePlayers());
	
	protected AllPlayerList(SubType subtype) {
		super(subtype);
	}

	@Override
	public String getText(OfflinePlayer player) {
		return subtype.getText(player, allPlayers);
	}

}
