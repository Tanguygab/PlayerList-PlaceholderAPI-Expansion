package io.github.tanguygab.playerlistexpansion.subtype;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import us.myles.ViaVersion.api.Via;

public class Version extends SubType {

	public Version(boolean countSelf, String output, String subtypevalue) {
		super(countSelf, output, subtypevalue);
	}

	@Override
	public String getText(OfflinePlayer player, Collection<? extends OfflinePlayer> playerList) {
		if (!Bukkit.getPluginManager().isPluginEnabled("ViaVersion")) {
			return "&4&lError: &cYou need ViaVersion!";
		}
		ArrayList<String> playersVersion = new ArrayList<>();
		for (OfflinePlayer p : playerList) {
			if (countSelf || !p.getName().equals(player.getName())) {
				for (String version : subTypeValue.split("\\+")) {
					Integer pVer = Via.getAPI().getPlayerVersion(p.getUniqueId());
					if (!playersVersion.contains(p.getName())) {
						if (version.contains("-")) {
							String[] verRange = version.split("-");
							if (pVer >= Integer.parseInt(verRange[0]) && pVer <= Integer.parseInt(verRange[1])) {
								playersVersion.add(p.getName());
							}
						} else if (pVer == Integer.parseInt(version)) {
							playersVersion.add(p.getName());
						}
					}
				}
			}
		}
		return format(player, playersVersion);
	}
}