package io.github.tanguygab.playerlistexpansion;

import io.github.tanguygab.playerlistexpansion.filters.Filter;
import org.bukkit.OfflinePlayer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerList {

    private final ListType type;
    private final List<Filter> filters;
    private final Boolean included;

    public PlayerList(ListType type, List<Filter> filters, boolean included) {
        this.type = type;
        this.filters = filters;
        this.included = included;
    }

    public String getText(OfflinePlayer viewer, String arg) {
        List<OfflinePlayer> list = getList(viewer);
        Map<String,OfflinePlayer> sortedMap = new TreeMap<>();
        list.forEach(p->sortedMap.put(p.getName(),p));
        list = new ArrayList<>(sortedMap.values());

        switch (arg) {
            case "list": return list.stream().map(OfflinePlayer::getName).collect(Collectors.joining(", "));
            case "amount": return list.size()+"";
            default: {
                int pos;
                try {pos = Integer.parseInt(arg);}
                catch (Exception e) {return null;}
                return pos >= 0 && pos < list.size() ? list.get(pos).getName() : PlayerListExpansion.get().offlineText;
            }
        }
    }

    private List<OfflinePlayer> getList(OfflinePlayer viewer) {
        List<OfflinePlayer> list = type.getList();
        if (!included) list.remove(viewer);
        Stream<OfflinePlayer> stream = list.stream();
        for (Filter filter : filters) stream = filter.filter(stream,viewer);
        return stream.collect(Collectors.toList());
    }

}
