package io.github.tanguygab.playerlistexpansion;

import io.github.tanguygab.playerlistexpansion.filters.Filter;
import io.github.tanguygab.playerlistexpansion.sorting.SortingType;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerList {

    private final String name;
    private final ListType type;
    private final List<Filter> filters;
    private final List<SortingType> sortingTypes;
    private final boolean included;
    private final boolean duplicates;

    public PlayerList(String name, ListType type, List<Filter> filters, List<SortingType> sortingTypes, boolean included, boolean duplicates) {
        this.name = name;
        this.type = type;
        this.filters = filters;
        this.sortingTypes = sortingTypes;
        this.included = included;
        this.duplicates = duplicates;
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
            if (input.isEmpty()) list = new ArrayList<>();
            else {
                String separator = PlayerListExpansion.get().getString("lists." + name + ".separator", ",");
                list = new ArrayList<>(Arrays.asList(input.split(separator)));
            }
        } else list = type.getList().stream().map(OfflinePlayer::getName).collect(Collectors.toCollection(ArrayList::new));

        if (!included) list.remove(viewer.getName());
        if (!duplicates) {
            Set<String> set = new LinkedHashSet<>(list);
            list.clear();
            list.addAll(set);
        }

        filters.forEach(filter -> list.removeIf(name -> filter.isInverted() == filter.filter(name,viewer)));

        return sort(list,viewer);
    }

    private List<String> sort(List<String> list, OfflinePlayer viewer) {
        if (sortingTypes.isEmpty()) return list;

        Map<String,List<String>> sortedMap = new TreeMap<>();
        list.forEach(name->{
            StringBuilder sortingString = new StringBuilder();
            sortingTypes.forEach(type -> sortingString.append(type.getSortingString(name,viewer)));
            sortedMap.computeIfAbsent(sortingString.toString(),k->new ArrayList<>()).add(name);
        });
        return sortedMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

}
