package io.github.tanguygab.playerlistexpansion;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

import java.util.*;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.milkbowl.vault.permission.Permission;

import static org.apache.commons.lang.StringUtils.isNumeric;

public class PlayerListExpansion extends PlaceholderExpansion {
    Collection<? extends Player> listOnline = Bukkit.getServer().getOnlinePlayers();
    OfflinePlayer[] listOffline = Bukkit.getServer().getOfflinePlayers();
    OfflinePlayer[] listType = listOffline;

    @Override
    public boolean canRegister() {return true;}

    @Override
    public String getAuthor() {return "Tanguygab";}

    @Override
    public String getIdentifier() {return "playerlist";}

    @Override
    public String getVersion() {return "1.0.0";}

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {

        List<String> args = new ArrayList<>(Arrays.asList(identifier.split(",")));
            args = PlaceholderAPI.setBracketPlaceholders(player, args);

            if (!args.get(0).equals("online") && !args.get(0).equals("offline") && !args.get(0).equals("all")) {
                return "&3&lValid Syntax: &9%" + "playerlist_&b<list type>&9,&b<subtype>&9,&b<yes/no>&9,&b<output>&9,&b<subtype value>&9%\n" + "&3&lValid List Types: &9online&f, &9offline&f, &9all&f.";
            } else if (args.size() == 1 || !args.get(1).equals("normal") && !args.get(1).equals("perm") && !args.get(1).equals("world") && !args.get(1).equals("nearby")) {
                return "&3&lValid List SubTypes: &9normal&f, &9perm&f, &9world&f, &9nearby&f.";
            } else if (args.size() == 2 || !args.get(2).equals("yes") && !args.get(2).equals("no")) {
                return "&4&lError&c: You have to use either &9yes &cor &9no &cin the third argument.";
            } else if (args.size() == 3 || !args.get(3).equals("list") && !args.get(3).equals("amount") && !isNumeric(args.get(3))) {
                return "&3&lValid Output Types: &9list&f, &9amount&f, &9a number starting from 0&f.";
            } else if (args.size() == 4 && (args.get(1).equals("world") || args.get(1).equals("perm") || args.get(1).equals("nearby"))) {
                return "&4&lError&c: &cYou have to specify a &9permission&c/&9world&c/&9radius&c.";
            } else if (args.get(1).equals("nearby") && !isNumeric(args.get(4))) {
                return "&4&lError&c: &cYou have to provide a number for the radius!";
            } else if ((args.get(0).equals("offline") || args.get(0).equals("all")) && (args.get(1).equals("world") || args.get(1).equals("nearby"))) {
                return "&cUnsupported =/";
            }

        switch (args.get(1)) {
            case "normal":
                if (args.get(0).equals("online")) {listType = listOnline.toArray(new OfflinePlayer[0]);}
                else {listType = listOffline;}
                ArrayList<String> players = new ArrayList<>();
                for (OfflinePlayer p : listType) {
                    if (args.get(2).equals("no") && p.getName().equals(player.getName())) {}
                    else {
                        if (args.get(0).equals("offline") && listOnline.contains(p)) {}
                        else {players.add(p.getName());}
                    }
                }
                Collections.sort(players);

                if (args.get(3).equals("list")) {return players.toString().replace("[", "").replace("]", "");}
                else if (args.get(3).equals("amount")) {return players.size()+"";}
                else if (players.size() != 0 && Integer.parseInt(args.get(3)) < players.size()) {return players.get(Integer.parseInt(args.get(3)))+"";}
                else {return "Offline";}

            case "perm":
                ArrayList<String> playersPerm = new ArrayList<>();
                for (Player p : listOnline) {
                    if (args.get(2).equals("no") && p.getName().equals(player.getName())) {}
                    else {
                        for (String perm : args.get(4).split("\\+"))
                            if (!playersPerm.contains(p.getName()) && p.hasPermission(perm)) {playersPerm.add(p.getName());}
                    }
                }
                Collections.sort(playersPerm);

                if (args.get(3).equals("list")) {return playersPerm.toString().replace("[", "").replace("]", "");}
                else if (args.get(3).equals("amount")) {return playersPerm.size()+"";}
                else if (playersPerm.size() != 0 && Integer.parseInt(args.get(3)) < playersPerm.size()) {return playersPerm.get(Integer.parseInt(args.get(3)))+"";}
                else {return "Offline";}

            case "world":
                ArrayList<String> playersWorld = new ArrayList<>();
                for (Player p : listOnline) {
                    if (args.get(2).equals("no") && p.getName().equals(player.getName())) {}
                    else {
                        if (("+"+args.get(4)+"+").contains("+"+p.getWorld().getName()+"+")) {playersWorld.add(p.getName());}
                    }
                }
                Collections.sort(playersWorld);

                if (args.get(3).equals("list")) {return playersWorld.toString().replace("[", "").replace("]", "");}
                else if (args.get(3).equals("amount")) {return playersWorld.size()+"";}
                else if (playersWorld.size() != 0 && Integer.parseInt(args.get(3)) < playersWorld.size()) {return playersWorld.get(Integer.parseInt(args.get(3)))+"";}
                else {return "Offline";}

            case "nearby":
                Integer zone = Integer.parseInt(args.get(4))*Integer.parseInt(args.get(4));
                ArrayList<String> playersNearby = new ArrayList<>();
                for (Player p : listOnline) {
                    if (args.get(2).equals("no") && p.getName().equals(player.getName())) {}
                    else {
                        if (p.getWorld().equals(player.getPlayer().getWorld()) && player.getPlayer().getLocation().distanceSquared(p.getLocation()) < zone) {playersNearby.add(p.getName());}
                    }
                }
                Collections.sort(playersNearby);

                if (args.get(3).equals("list")) {return playersNearby.toString().replace("[", "").replace("]", "");}
                else if (args.get(3).equals("amount")) {return playersNearby.size()+"";}
                else if (playersNearby.size() != 0 && Integer.parseInt(args.get(3)) < playersNearby.size()) {return playersNearby.get(Integer.parseInt(args.get(3)))+"";}
                else {return "Offline";}

        }

        return "Offline";
    }
}