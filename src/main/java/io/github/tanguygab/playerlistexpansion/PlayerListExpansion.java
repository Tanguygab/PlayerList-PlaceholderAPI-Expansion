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
    public String getVersion() {return "1.3";}

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {

        List<String> args = new ArrayList<>(Arrays.asList(identifier.split(",")));
            for (Integer i = 0; i < args.size(); i++) {
                if (args.get(i).startsWith("[") && args.get(i).endsWith("]")) {
                    args.set(i, PlaceholderAPI.setPlaceholders(player, args.get(i).replaceFirst("\\[", "%").substring(0,args.get(i).length()-1)+"%"));
                }
            }
            for (Integer i = 0; i < args.size(); i++) {
                if (args.get(i).startsWith("{") && args.get(i).endsWith("}")) {
                    args.set(i, PlaceholderAPI.setPlaceholders(player, args.get(i).replaceFirst("\\{", "%").substring(0,args.get(i).length()-1)+"%"));
                }
            }
            if (args.size() >= 4 && args.get(3).equals("list")) {args.set(3, "list-\\. ");}

            if (!args.get(0).equals("online") && !args.get(0).equals("offline") && !args.get(0).equals("all")) {
                return "&3&lValid Syntax: &9%" + "playerlist_&b<list type>&9,&b<subtype>&9,&b<yes/no>&9,&b<output>&9,&b<subtype value>&9%\n" + "&3&lValid List Types: &9online&f, &9offline&f, &9all&f.";
            } else if (args.size() == 1 || !args.get(1).equals("normal") && !args.get(1).equals("perm") && !args.get(1).equals("world") && !args.get(1).equals("nearby") && !args.get(1).equals("whitelisted") && !args.get(1).equals("banned") && !args.get(1).equals("cansee")) {
                return "&3&lValid List SubTypes: &9normal&f, &9perm&f, &9world&f, &9nearby&f &9whitelisted&f, &9banned&f, &9cansee&f.";
            } else if (args.size() == 2 || !args.get(2).equals("yes") && !args.get(2).equals("no")) {
                return "&4&lError&c: You have to use either &9yes &cor &9no &cin the third argument.";
            } else if (args.size() == 3 || !args.get(3).startsWith("list") && !args.get(3).equals("amount") && !isNumeric(args.get(3))) {
                return "&3&lValid Output Types: &9list&f, &9amount&f, &9a number starting from 0&f.";
            } else if (args.get(3).startsWith("list") && !args.get(3).startsWith("list-")) {
                return "&4&lError: &cUse `&9list-<separator>&c` to change the characters between names in the list. Use &9\\. &cfor &9,";
            } else if (args.size() == 4 && (args.get(1).equals("world") || args.get(1).equals("perm") || args.get(1).equals("nearby"))) {
                return "&4&lError&c: &cYou have to specify a &9permission&c/&9world&c/&9radius&c.";
            } else if (args.get(1).equals("nearby") && !isNumeric(args.get(4))) {
                return "&4&lError&c: &cYou have to provide a number for the radius!";
            } else if ((args.get(0).equals("offline") || args.get(0).equals("all")) && (args.get(1).equals("world") || args.get(1).equals("nearby") || args.get(1).equals("cansee") || args.get(1).equals("locale") || args.get(1).equals("version"))) {
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

                if (args.get(3).startsWith("list")) {return players.toString().replace("[", "").replace("]", "").replace(", ", args.get(3).replaceFirst("list-", "")).replace("\\.", ",");}
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

                if (args.get(3).startsWith("list")) {return playersPerm.toString().replace("[", "").replace("]", "").replace(", ", args.get(3).replaceFirst("list-", "")).replace("\\.", ",");}
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

                if (args.get(3).startsWith("list")) {return playersWorld.toString().replace("[", "").replace("]", "").replace(", ", args.get(3).replaceFirst("list-", "")).replace("\\.", ",");}
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

                if (args.get(3).startsWith("list")) {return playersNearby.toString().replace("[", "").replace("]", "").replace(", ", args.get(3).replaceFirst("list-", "")).replace("\\.", ",");}
                else if (args.get(3).equals("amount")) {return playersNearby.size()+"";}
                else if (playersNearby.size() != 0 && Integer.parseInt(args.get(3)) < playersNearby.size()) {return playersNearby.get(Integer.parseInt(args.get(3)))+"";}
                else {return "Offline";}

            case "whitelisted":
                if (args.get(0).equals("online")) {listType = listOnline.toArray(new OfflinePlayer[0]);}
                else {listType = listOffline;}
                ArrayList<String> playersWhitelisted = new ArrayList<>();
                for (OfflinePlayer p : listType) {
                    if (args.get(2).equals("no") && p.getName().equals(player.getName())) {}
                    else {
                        if (args.get(0).equals("offline") && listOnline.contains(p)) {}
                        else {
                            if (p.isWhitelisted()) {
                                playersWhitelisted.add(p.getName());
                            }
                        }
                    }
                }
                Collections.sort(playersWhitelisted);

                if (args.get(3).startsWith("list")) {return playersWhitelisted.toString().replace("[", "").replace("]", "").replace(", ", args.get(3).replaceFirst("list-", "")).replace("\\.", ",");}
                else if (args.get(3).equals("amount")) {return playersWhitelisted.size()+"";}
                else if (playersWhitelisted.size() != 0 && Integer.parseInt(args.get(3)) < playersWhitelisted.size()) {return playersWhitelisted.get(Integer.parseInt(args.get(3)))+"";}
                else {return "Offline";}

            case "banned":
                if (args.get(0).equals("online")) {listType = listOnline.toArray(new OfflinePlayer[0]);}
                else {listType = listOffline;}
                ArrayList<String> playersBanned = new ArrayList<>();
                for (OfflinePlayer p : listType) {
                    if (args.get(2).equals("no") && p.getName().equals(player.getName())) {}
                    else {
                        if (args.get(0).equals("offline") && listOnline.contains(p)) {}
                        else {
                            if (p.isBanned()) {
                                playersBanned.add(p.getName());
                            }
                        }
                    }
                }
                Collections.sort(playersBanned);

                if (args.get(3).startsWith("list")) {return playersBanned.toString().replace("[", "").replace("]", "").replace(", ", args.get(3).replaceFirst("list-", "")).replace("\\.", ",");}
                else if (args.get(3).equals("amount")) {return playersBanned.size()+"";}
                else if (playersBanned.size() != 0 && Integer.parseInt(args.get(3)) < playersBanned.size()) {return playersBanned.get(Integer.parseInt(args.get(3)))+"";}
                else {return "Offline";}

            case "cansee":
                ArrayList<String> playersCanSee = new ArrayList<>();
                for (Player p : listOnline) {
                    if (args.get(2).equals("no")) {
                        if (!player.getPlayer().canSee(p) && !p.getName().equals(player.getName())) {
                            playersCanSee.add(p.getName());
                        }
                    }
                    else {
                        if (player.getPlayer().canSee(p) && !p.getName().equals(player.getName())) {
                            playersCanSee.add(p.getName());
                        }
                    }
                }
                Collections.sort(playersCanSee);

                if (args.get(3).startsWith("list")) {return playersCanSee.toString().replace("[", "").replace("]", "").replace(", ", args.get(3).replaceFirst("list-", "")).replace("\\.", ",");}
                else if (args.get(3).equals("amount")) {return playersCanSee.size()+"";}
                else if (playersCanSee.size() != 0 && Integer.parseInt(args.get(3)) < playersCanSee.size()) {return playersCanSee.get(Integer.parseInt(args.get(3)))+"";}
                else {return "Offline";}
        }

        return "Offline";
    }
}