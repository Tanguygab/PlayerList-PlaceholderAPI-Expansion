package io.github.tanguygab.playerlistexpansion;

import io.github.tanguygab.playerlistexpansion.filters.Filter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerList {

    private final String name;
    private final ListType type;
    private final List<Filter> filters;
    private final Boolean included;

    public PlayerList(String name, ListType type, List<Filter> filters, boolean included) {
        this.name = name;
        this.type = type;
        this.filters = filters;
        this.included = included;
    }

    public String getText(OfflinePlayer viewer, String arg) {
        List<String> names = getList(viewer);

        if (arg.equals("amount")) return names.size()+"";

        if (arg.startsWith("list")) {
            String separator = arg.startsWith("list-") ? arg.substring(5).replace("\\n","\n") : ", ";
            return String.join(separator,names);
        }

        int pos;
        try {pos = Integer.parseInt(arg);}
        catch (Exception e) {return null;}
        return pos >= 0 && pos < names.size() ? names.get(pos) : PlayerListExpansion.get().offlineText;
    }

    private List<String> getList(OfflinePlayer viewer) {
        if (type == ListType.CUSTOM) {
            String input = PlaceholderAPI.setPlaceholders(viewer,PlayerListExpansion.get().getString("lists."+name+".input",""));
            String separator = PlayerListExpansion.get().getString("lists."+name+".separator",",");
            List<String> list = Arrays.asList(input.split(separator));
            if (!included) {
                list = new ArrayList<>(list);
                list.remove(viewer.getName());
            }
            return list;
        }

        List<OfflinePlayer> list = type.getList();
        if (!included) list.remove(viewer);
        Stream<OfflinePlayer> stream = list.stream();
        for (Filter filter : filters) stream = filter.filter(stream,viewer);

        Map<String,OfflinePlayer> sortedMap = new TreeMap<>();
        stream.forEach(p->sortedMap.put(p.getName(),p));
        return sortedMap.values()
                .stream()
                .map(OfflinePlayer::getName)
                .collect(Collectors.toList());
    }

}
