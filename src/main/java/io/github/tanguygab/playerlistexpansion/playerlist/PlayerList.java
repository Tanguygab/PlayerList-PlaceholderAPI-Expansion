package io.github.tanguygab.playerlistexpansion.playerlist;

import java.util.*;
import static org.apache.commons.lang.StringUtils.isNumeric;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import io.github.tanguygab.playerlistexpansion.subtype.*;

public abstract class PlayerList {

	protected SubType subtype;

	protected PlayerList() {}

	protected PlayerList(SubType subtype) {
		this.subtype = subtype;
	}

	public abstract String getText(OfflinePlayer p);

	public static PlayerList compile(String text) {
		List<String> args = new ArrayList<>(Arrays.asList(text.split(",")));

        if (args.size() >= 4 && args.get(3).equals("list")) {
            args.set(3, "list-\\. ");
		}

		if (!args.get(0).equals("online") && !args.get(0).equals("offline") && !args.get(0).equals("all")) {
			return new InvalidPlayerList("&3&lValid Syntax: &9%" + "playerlist_&b<list type>&9,&b<subtype>&9,&b<yes/no>&9,&b<output>&9,&b<subtype value>&9%\n" + "&3&lValid List Types: &9online&f, &9offline&f, &9all&f.");
		} else if (args.size() == 1 || !args.get(1).equals("normal") && !args.get(1).equals("perm") && !args.get(1).equals("world") && !args.get(1).equals("nearby") && !args.get(1).equals("whitelisted") && !args.get(1).equals("banned") && !args.get(1).equals("cansee") && !args.get(1).equals("placeholder") && !args.get(1).equals("version") && !args.get(1).equals("gamemode")) {
			return new InvalidPlayerList("&3&lValid List SubTypes: &9normal&f, &9perm&f, &9world&f, &9nearby&f &9whitelisted&f, &9banned&f, &9cansee&f, &9placeholder&f, &9version&f, &9gamemode&f.");
		} else if (args.size() == 2 || !args.get(2).equals("yes") && !args.get(2).equals("no")) {
			return new InvalidPlayerList("&4&lError&c: You have to use either &9yes &cor &9no &cin the third argument.");
		} else if (args.size() == 3 || !args.get(3).startsWith("list") && !args.get(3).equals("amount") && !isNumeric(args.get(3)) && !args.get(3).startsWith("{")) {
			return new InvalidPlayerList("&3&lValid Output Types: &9list&f, &9amount&f, &9a number starting from 0&f.");
		} else if (args.get(3).startsWith("list") && !args.get(3).startsWith("list-")) {
			return new InvalidPlayerList("&4&lError: &cUse `&9list-<separator>&c` to change the characters between names in the list. Use &9\\. &cfor &9,");
		} else if (args.size() == 4 && (args.get(1).equals("world") || args.get(1).equals("perm") || args.get(1).equals("nearby") || args.get(1).equals("placeholder") || args.get(1).equals("version") || args.get(1).equals("gamemode"))) {
			return new InvalidPlayerList("&4&lError&c: &cYou have to specify a &9permission&c/&9world&c/&9radius/placeholder/version/gamemode&c.");
		} else if (args.get(1).equals("placeholder") && !args.get(4).contains("||")) {
			return new InvalidPlayerList("&4&lError&c: &cYou have to provide an output to check separated by || !");
		} else if ((args.get(0).equals("offline") || args.get(0).equals("all")) && (args.get(1).equals("world") || args.get(1).equals("nearby") || args.get(1).equals("cansee") || args.get(1).equals("version") || args.get(1).equals("gamemode"))) {
			return new InvalidPlayerList("&cUnsupported =/");
		}

		String type = args.get(0);

		String subTypeValue = args.size() > 4 ? args.get(4) : null;
		SubType subType = SubType.compile(args.get(1), args.get(2).equalsIgnoreCase("yes"), args.get(3), subTypeValue);

		if (type.equals("online")) {
			return new OnlinePlayerList(subType);
		}
		if (type.equals("offline")) {
			return new OfflinePlayerList(subType);
		}
		if (type.equals("all")) {
			return new AllPlayerList(subType);
		}
		return new InvalidPlayerList(text);
	}
}
