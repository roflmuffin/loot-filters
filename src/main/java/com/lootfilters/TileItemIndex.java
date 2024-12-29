package com.lootfilters;

import net.runelite.api.Tile;
import net.runelite.api.TileItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TileItemIndex {
    private final Map<Tile, List<TileItem>> index = new HashMap<>();

    public Set<Map.Entry<Tile, List<TileItem>>> entrySet() {
        return index.entrySet();
    }

    public void put(Tile tile, TileItem item) {
        if (!index.containsKey(tile)) {
            index.put(tile, new ArrayList<>());
        }
        index.get(tile).add(item);
    }

    public void remove(Tile tile, TileItem item) {
        if (!index.containsKey(tile)) {
            return; // what?
        }

        var items = index.get(tile);
        items.remove(item);
        if (items.isEmpty()) {
            index.remove(tile);
        }
    }

    public void clear() {
        index.clear();
    }
}
