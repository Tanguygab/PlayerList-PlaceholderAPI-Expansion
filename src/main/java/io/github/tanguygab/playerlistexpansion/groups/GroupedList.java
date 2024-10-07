package io.github.tanguygab.playerlistexpansion.groups;

import io.github.tanguygab.playerlistexpansion.PlayerList;

public class GroupedList {

    private final PlayerList list;
    private final String title;
    private final int max;
    private final String remaining;

    public GroupedList(PlayerList list, String title, int max, String remaining) {
        this.list = list;
        this.title = title;
        this.max = max;
        this.remaining = remaining;
    }

    public PlayerList getList() {
        return list;
    }

    public String getTitle() {
        return title;
    }

    public int getMax() {
        return max;
    }

    public String getRemaining() {
        return remaining;
    }
}
