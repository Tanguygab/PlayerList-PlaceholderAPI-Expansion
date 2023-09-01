package io.github.tanguygab.playerlistexpansion.sorting;

import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class SortingType {


    private static final Map<String, Function<String, SortingType>> types = new HashMap<String,Function<String,SortingType>>() {{
        put("a_to_z",arg->new A_TO_Z());
        put("z_to_a",arg->new Z_TO_A());
    }};

    public static SortingType find(String type, String arg) {
        return types.containsKey(type.toLowerCase()) ? types.get(type.toLowerCase()).apply(arg) : null;
    }

    public abstract String getSortingString(String name, OfflinePlayer viewer);

}
