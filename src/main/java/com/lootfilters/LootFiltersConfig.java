package com.lootfilters;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

import java.awt.Color;

@ConfigGroup("loot-filters")
public interface LootFiltersConfig extends Config {
    @ConfigSection(
            name = "General",
            description = "Configure general options.",
            position = 0
    )
    String general = "general";
    @ConfigItem(
            keyName = "filterConfig",
            name = "Filter config",
            description = "The filter config.",
            section = general,
            position = 0
    )
    default String filterConfig() { return ""; }
    @ConfigItem(
            keyName = "showUnmatchedItems",
            name = "Show unmatched items",
            description = "By default, items that do not match a filter rule will not get a text overlay. Enable this to instead show the overlay as a fallback when an item doesn't match a filter rule.",
            section = general,
            position = 1
    )
    default boolean showUnmatchedItems() { return false; }
    @ConfigItem(
            keyName = "ownershipFilter",
            name = "Ownership filter",
            description = "When enabled, filters out any items you cannot pick up. This filter is ABSOLUTE, and overrides ALL other rules, including default highlight/hide, default item value rules, and the active loot filter.",
            section = general,
            position = 2
    )
    default boolean ownershipFilter() { return false; }

    @ConfigSection(
            name = "Display overrides",
            description = "Configure global display overrides.",
            position = 1
    )
    String displayOverrides = "displayOverrides";
    @ConfigItem(
            keyName = "alwaysShowValue",
            name = "Always show value",
            description = "Always show item value.",
            section = displayOverrides,
            position = 0
    )
    default boolean alwaysShowValue() { return false; }
    @ConfigItem(
            keyName = "alwaysShowDespawn",
            name = "Always show despawn",
            description = "Always show item despawn timers.",
            section = displayOverrides,
            position = 1
    )
    default boolean alwaysShowDespawn() { return false; }

    @ConfigSection(
            name = "Item lists",
            description = "Configure default lists of highlighted and hidden items. Values are case-insensitive, separated by comma.",
            position = 2
    )
    String itemLists = "itemLists";
    @ConfigItem(
            keyName = "highlightedItems",
            name = "Highlighted items",
            description = "Configure a list of items to highlight.",
            section = itemLists,
            position = 0
    )
    default String highlightedItems() { return ""; }
    @ConfigItem(
            keyName = "hiddenItems",
            name = "Hidden items",
            description = "Configure a list of items to hide.",
            section = itemLists,
            position = 1
    )
    default String hiddenItems() { return ""; }
    @ConfigItem(
            keyName = "highlightColor",
            name = "Highlight color",
            description = "Configures the color for highlighted items.",
            section = itemLists,
            position = 2
    )
    default Color highlightColor() { return Color.decode("#aa00ff"); }

    @ConfigSection(
            name = "Item value rules",
            description = "Configure default rules for showing based on item value. These rules are checked AFTER both the active filter and the global hide list.",
            position = 2
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
