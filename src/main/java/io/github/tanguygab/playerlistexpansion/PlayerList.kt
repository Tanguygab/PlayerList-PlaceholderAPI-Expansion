package io.github.tanguygab.playerlistexpansion;

import io.github.tanguygab.playerlistexpansion.filters.Filter;
import io.github.tanguygab.playerlistexpansion.sorting.SortingType;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerList {

    private final String name;
    private final ListType type;
    private final List<Filter> filters;
    private final List<SortingType> sortingTypes;
    private final boolean included;
    private final boolean duplicates;
    private final Map<OfflinePlayer, CachedList> lastUpdate = new WeakHashMap<>();

    public PlayerList(String name, ListType type, List<Filter> filters, List<SortingType> sortingTypes, boolean included, boolean duplicates) {
        this.name = name;
        this.type = type;
        this.filters = filters;
        this.sortingTypes = sortingTypes;
        this.included = included;
        this.duplicates = duplicates;
    }

    public String getText(OfflinePlayer viewer, String arguments) {
        String[] args = arguments.split("_");
        String format = PlayerListExpansion.get().getFormat(arguments, args);
        String arg = args[0];

        List<String> names = lastUpdate.computeIfAbsent(viewer, k -> new CachedList(this::getList)).getList(viewer, format);

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

    public List<String> getList(OfflinePlayer viewer, String format) {
        Stream<String> stream;
        if (type == ListType.CUSTOM) {
            String input = PlaceholderAPI.setPlaceholders(viewer,PlayerListExpansion.get().getString("lists."+name+".input",""));
            if (input.isEmpty()) stream = Stream.of();
            else {
                String separator = PlayerListExpansion.get().getString("lists." + name + ".separator", ",");
                stream = Stream.of(input.split(separator));
            }
        } else stream = type.getList()
                .stream()
                .map(OfflinePlayer::getName);

        stream = filter(viewer, format, stream);

        return sort(stream.collect(Collectors.toList()), viewer);
    }

    private Stream<String> filter(OfflinePlayer viewer, String format, Stream<String> stream) {
        if (!included) stream = stream.filter(name -> !name.equals(viewer.getName()));
        if (!duplicates) stream = stream.distinct();

        stream = stream.filter(name -> filters
                .stream()
                .noneMatch(filter -> filter.isInverted() == filter.filter(name,viewer)));

        stream = stream.map(name -> {
            OfflinePlayer player = Bukkit.getOfflinePlayer(name);
            if (!player.hasPlayedBefore() && !player.isOnline()) return name;
            return PlaceholderAPI.setBracketPlaceholders(player, format);
        });

        return stream;
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
