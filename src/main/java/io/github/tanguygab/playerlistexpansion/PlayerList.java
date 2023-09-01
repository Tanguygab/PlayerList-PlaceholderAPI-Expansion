package io.github.tanguygab.playerlistexpansion;

import io.github.tanguygab.playerlistexpansion.filters.Filter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerList {

    private final String name;
    private final ListType type;
    private final List<Filter> filters;
    private final boolean included;

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
        List<String> list;
        if (type == ListType.CUSTOM) {
            String input = PlaceholderAPI.setPlaceholders(viewer,PlayerListExpansion.get().getString("lists."+name+".input",""));
            String separator = PlayerListExpansion.get().getString("lists."+name+".separator",",");
            list = new ArrayList<>(Arrays.asList(input.split(separator)));
        } else list = type.getList().stream().map(OfflinePlayer::getName).collect(Collectors.toCollection(ArrayList::new));

        if (!included) list.remove(viewer.getName());
        for (Filter filter : filters)
            list.removeIf(name->!filter.filter(name,viewer));

        if (type != ListType.CUSTOM) Collections.sort(list);
        return list;
    }

}
