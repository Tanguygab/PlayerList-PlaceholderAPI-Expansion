package io.github.tanguygab.playerlistexpansion.playerlist;

import org.bukkit.OfflinePlayer;

public class InvalidPlayerList extends PlayerList {

	private String errorMessage;
	
	public InvalidPlayerList(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String getText(OfflinePlayer p) {
		return errorMessage;
	}
}