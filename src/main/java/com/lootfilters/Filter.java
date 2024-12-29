package com.lootfilters;

import net.runelite.api.TileItem;

public class Filter {
    private final Rule rule;
    private final DisplayConfig display;

    public Filter(Rule rule, DisplayConfig display) {
        this.rule = rule;
        this.display = display;
    }

    public boolean test(TileItem item) {
        return rule.test(item);
    }

    public DisplayConfig getDisplay() {
        return display;
    }
}
