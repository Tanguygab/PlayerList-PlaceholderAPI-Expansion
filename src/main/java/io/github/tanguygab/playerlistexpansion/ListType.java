package io.github.tanguygab.playerlistexpansion;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public enum ListType {

    ONLINE(()-> Bukkit.getServer().getOnlinePlayers()),
    OFFLINE(()-> Arrays.stream(Bukkit.getServer().getOfflinePlayers()).filter(p->!p.isOnline()).collect(Collectors.toList())),
    ALL(()-> Arrays.asList(Bukkit.getServer().getOfflinePlayers())),
    CUSTOM;

    Callable<Collection<? extends OfflinePlayer>> callable;
    ListType() {}
    ListType(Callable<Collection<? extends OfflinePlayer>> callable) {
        this.callable = callable;
    }

    public Collection<? extends OfflinePlayer> getList() {
        try {return callable.call();}
        catch (Exception e) {throw new RuntimeException(e);}
    }

    public static ListType find(String str) {
        if (str == null) return null;
        str = str.toUpperCase();
        for (ListType type : values())
            if (type.toString().equals(str))
                return type;
        return null;
    }

}
