package io.github.tanguygab.playerlistexpansion;

import java.lang.reflect.Array;
import java.util.*;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;

import io.github.tanguygab.playerlistexpansion.playerlist.PlayerList;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PlayerListExpansion extends PlaceholderExpansion {

	private Map<String, PlayerList> map = new HashMap<String, PlayerList>();

	@Override
	public boolean canRegister() {
		return true;
	}

	@Override
	public String getAuthor() {
		return "Tanguygab";
	}

	@Override
	public String getIdentifier() {
		return "playerlist";
	}

	@Override
	public String getVersion() {
		return "2.1";
	}

	@Override
	public String onRequest(OfflinePlayer player, String identifier) {
		List<String> args = new ArrayList<>(Arrays.asList(identifier.split(",")));

		for (Integer i = 0; i < args.size(); i++) {
			if (args.get(i).startsWith("[") && args.get(i).endsWith("]")) {
				args.set(i, PlaceholderAPI.setPlaceholders(player, args.get(i).replaceFirst("\\[", "%").substring(0, args.get(i).length() - 1) + "%"));
			} else if (args.get(i).startsWith("{") && args.get(i).endsWith("}")) {
				args.set(i, PlaceholderAPI.setPlaceholders(player, args.get(i).replaceFirst("\\{", "%").substring(0, args.get(i).length() - 1) + "%"));
			}
		}
			identifier = args.toString().substring(0, args.toString().length()-1).replaceFirst("\\[", "").replace(", ",",");

		if (!map.containsKey(identifier)) {
			map.put(identifier, PlayerList.compile(identifier));
		}
		return map.get(identifier).getText(player);
	}
}
