package io.github.tanguygab.playerlistexpansion.playerlist;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import io.github.tanguygab.playerlistexpansion.subtype.SubType;

public class OnlinePlayerList extends PlayerList {

	private Collection<? extends Player> onlinePlayers = Bukkit.getServer().getOnlinePlayers();
	
	public OnlinePlayerList(SubType subtype) {
		super(subtype);
	}

	@Override
	public String getText(OfflinePlayer p) {
		return subtype.getText(p, onlinePlayers);
	}
}