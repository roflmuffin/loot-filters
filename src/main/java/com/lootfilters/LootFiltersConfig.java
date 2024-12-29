package com.lootfilters;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("loot-filters")
public interface LootFiltersConfig extends Config {
    @ConfigItem(
            keyName = "filterConfig",
            name = "Filter config",
            description = "The filter config syntax. Currently only supports JSON."
    )
    default String filterConfig()
    {
        return "[]";
    }
}
