package io.github.tanguygab.playerlistexpansion.playerlist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import io.github.tanguygab.playerlistexpansion.subtype.SubType;

public class OfflinePlayerList extends PlayerList {

	private List<OfflinePlayer> allPlayers = Arrays.asList(Bukkit.getOfflinePlayers());
	private Collection<? extends Player> onlinePlayers = Bukkit.getServer().getOnlinePlayers();
	
	protected OfflinePlayerList(SubType subtype) {
		super(subtype);
	}

	@Override
	public String getText(OfflinePlayer player) {
		Collection<OfflinePlayer> offlinePlayers = new ArrayList<>(allPlayers);
		offlinePlayers.removeAll(onlinePlayers);
		return subtype.getText(player, offlinePlayers);
	}

}
