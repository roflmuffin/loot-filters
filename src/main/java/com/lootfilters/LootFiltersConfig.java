package com.lootfilters;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

import java.awt.Color;

@ConfigGroup("loot-filters")
public interface LootFiltersConfig extends Config {
    @ConfigItem(
            keyName = "filterConfig",
            name = "Filter config",
            description = "The filter config syntax. Currently only supports JSON."
    )
    default String filterConfig() { return ""; }

    @ConfigSection(
            name = "Item value rules",
            description = "Configure global, default rules for matching item value.",
            position = 0
    )
    String itemValueRules = "itemValueRules";
    @ConfigItem(
            keyName = "enableInsaneItemValueTier",
            name = "Insane tier",
            description = "Enable INSANE item value tier.",
            section = itemValueRules,
            position = 1
    )
    default boolean enableInsaneItemValueTier() { return true; }
    @ConfigItem(
            keyName = "insaneValue",
            name = "Insane value",
            description = "Configures the value for INSANE tier.",
            section = itemValueRules,
            position = 2
    )
    default int insaneValue() { return 10_000_000; }
    @ConfigItem(
            keyName = "insaneValueColor",
            name = "Insane color",
            description = "Configures the color for INSANE item values.",
            section = itemValueRules,
            position = 3
    )
    default Color insaneValueColor() { return Color.decode("#ff66b2"); }
    @ConfigItem(
            keyName = "enableHighItemValueTier",
            name = "High tier",
            description = "Enable high item value tier.",
            section = itemValueRules,
            position = 4
    )
    default boolean enableHighItemValueTier() { return true; }
    @ConfigItem(
            keyName = "highValue",
            name = "High value",
            description = "Configures the value for high tier.",
            section = itemValueRules,
            position = 5
    )
    default int highValue() { return 1_000_000; }
    @ConfigItem(
            keyName = "highValueColor",
            name = "High color",
            description = "Configures the color for high item values.",
            section = itemValueRules,
            position = 6
    )
    default Color highValueColor() { return Color.decode("#ff9600"); }
    @ConfigItem(
            keyName = "enableMediumItemValueTier",
            name = "Medium tier",
            description = "Enable medium item value tier.",
            section = itemValueRules,
            position = 7
    )
    default boolean enableMediumItemValueTier() { return true; }
    @ConfigItem(
            keyName = "mediumValue",
            name = "Medium value",
            description = "Configures the value for medium tier.",
            section = itemValueRules,
            position = 8
    )
    default int mediumValue() { return 100_000; }
    @ConfigItem(
            keyName = "mediumValueColor",
            name = "Medium color",
            description = "Configures the color for medium item values.",
            section = itemValueRules,
            position = 9
    )
    default Color mediumValueColor() { return Color.decode("#99ff99"); }
    @ConfigItem(
            keyName = "enableLowItemValueTier",
            name = "Low tier",
            description = "Enable low item value tier.",
            section = itemValueRules,
            position = 10
    )
    default boolean enableLowItemValueTier() { return true; }
    @ConfigItem(
            keyName = "lowValue",
            name = "Low value",
            description = "Configures the value for low tier.",
            section = itemValueRules,
            position = 11
    )
    default int lowValue() { return 10_000; }
    @ConfigItem(
            keyName = "lowValueColor",
            name = "Low color",
            description = "Configures the color for low item values.",
            section = itemValueRules,
            position = 12
    )
    default Color lowValueColor() { return Color.decode("#66b2ff"); }
}
