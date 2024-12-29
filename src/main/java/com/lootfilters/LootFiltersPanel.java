package com.lootfilters;

import net.runelite.client.ui.PluginPanel;

public class LootFiltersPanel extends PluginPanel {
    private final LootFiltersPlugin plugin;

    public LootFiltersPanel(LootFiltersPlugin plugin) {
        this.plugin = plugin;

        setBorder(null);
    }
}