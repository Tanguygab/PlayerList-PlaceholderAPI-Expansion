package io.github.tanguygab.playerlistexpansion.sorting;

import org.bukkit.OfflinePlayer;

public class Z_TO_A extends SortingType {

    public Z_TO_A(String arg) {
        super(arg);
    }

    @Override
    public String getSortingString(String name, OfflinePlayer viewer) {
        char[] chars = parse(name,viewer).toCharArray();
        for (int i=0; i<chars.length; i++) {
            char c = chars[i];
            if (c >= 65 && c <= 90) chars[i] = (char) (155 - c);
            if (c >= 97 && c <= 122) chars[i] = (char) (219 - c);
        }
        return new String(chars);
    }
}
