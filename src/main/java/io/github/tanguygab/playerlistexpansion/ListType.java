package io.github.tanguygab.playerlistexpansion;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

public enum ListType {

    ONLINE(()-> new ArrayList<>(Bukkit.getServer().getOnlinePlayers())),
    OFFLINE(()-> Arrays.asList(Bukkit.getServer().getOfflinePlayers())),
    ALL(()->{
        List<OfflinePlayer> list = new ArrayList<>(Arrays.asList(Bukkit.getServer().getOfflinePlayers()));
        list.addAll(Bukkit.getServer().getOnlinePlayers());
        return list;
    });

    final Callable<List<OfflinePlayer>> callable;
    ListType(Callable<List<OfflinePlayer>> callable) {
        this.callable = callable;
    }

    public List<OfflinePlayer> getList() {
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
