package com.lootfilters;

import net.runelite.api.Tile;
import net.runelite.api.TileItem;

public class GroundItem {
    private final TileItem item;
    private final Tile tile;

    public GroundItem(TileItem item, Tile tile) {
        this.item = item;
        this.tile = tile;
    }

    public TileItem getItem() {
        return item;
    }

    public Tile getTile() {
        return tile;
    }
}
