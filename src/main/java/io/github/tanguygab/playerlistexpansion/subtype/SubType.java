package io.github.tanguygab.playerlistexpansion.subtype;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.OfflinePlayer;

public abstract class SubType {

	protected boolean countSelf;
	protected String output;
	protected String subTypeValue;

	public SubType(boolean countSelf, String output, String subTypeValue) {
		this.countSelf = countSelf;
		this.output = output;
		this.subTypeValue = subTypeValue;
	}


	protected String format(OfflinePlayer player, List<String> collection) {
		Collections.sort(collection);
		if (output.startsWith("list")) {
			return collection.toString().replace("[", "").replace("]", "").replace(", ", output.replaceFirst("list-", "")).replace("\\.", ",");
		} else if (output.equals("amount")) {
			return collection.size() + "";
		} else if (collection.size() != 0 && Integer.parseInt(output) < collection.size()) {
			return collection.get(Integer.parseInt(output)) + "";
		} else {
			return "Offline";
		}
	}

	public abstract String getText(OfflinePlayer player, Collection<? extends OfflinePlayer> playerList);
	
	public static SubType compile(String subtype, boolean countSelf, String output, String subtypevalue) {

		switch (subtype) {
		case "normal":
			return new Normal(countSelf, output, subtypevalue);
		case "perm":
			return new Permission(countSelf, output, subtypevalue);
		case "world":
			return new World(countSelf, output, subtypevalue);
		case "nearby":
			return new Nearby(countSelf, output, subtypevalue);
		case "whitelisted":
			return new WhiteListed(countSelf, output, subtypevalue);
		case "banned":
			return new Banned(countSelf, output, subtypevalue);
		case "cansee":
			return new CanSee(countSelf, output, subtypevalue);
		case "placeholder":
			return new Placeholder(countSelf, output, subtypevalue);
		case "version":
			return new Version(countSelf, output, subtypevalue);
		case "gamemode":
			return new Gamemode(countSelf, output, subtypevalue);
		default: 
			return null;
		}
	}
}
