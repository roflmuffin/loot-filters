package com.lootfilters;

import com.lootfilters.rule.ValueType;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Keybind;
import net.runelite.client.config.Units;

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
            keyName = "autoToggleFilter",
            name = "Auto-toggle active filter",
            description = "Filters can be annotated with area boundaries in which they are relevant. If enabled, filters will automatically become active when the player enters their corresponding area.",
            section = general,
            position = 0
    )
    default boolean autoToggleFilters() {
        return true;
    }
    @ConfigItem(
            keyName = "showUnmatchedItems",
            name = "Show unmatched items",
            description = "Give a default text overlay to items that don't match the active filter.",
            section = general,
            position = 1
    )
    default boolean showUnmatchedItems() { return true; }
    @ConfigItem(
            keyName = "ownershipFilter",
            name = "Ownership filter",
            description = "When enabled, filters out any items you cannot pick up. This filter is ABSOLUTE, and overrides ALL other rules, including default highlight/hide, default item value rules, and the active loot filter.",
            section = general,
            position = 3
    )
    default boolean ownershipFilter() { return false; }
    @ConfigItem(
            keyName = "valueType",
            name = "Value type",
            description = "The type of item value to use for rules and text overlay.",
            section = general,
            position = 4
    )
    default ValueType valueType() { return ValueType.HIGHEST; }

    @ConfigSection(
            name = "Hotkey",
            description = "Configure hotkey options.",
            position = 1
    )
    String hotkey = "Hotkey";
    @ConfigItem(
            keyName = "hotkey",
            name = "Hotkey",
            description = "Hotkey used by this plugin.",
            section = hotkey,
            position = 0
    )
    default Keybind hotkey() { return Keybind.ALT; }
    @ConfigItem(
            keyName = "hotkeyShowHiddenItems",
            name = "Press: Show hidden items",
            description = "Show hidden items when hotkey is pressed.",
            section = hotkey,
            position = 1
    )
    default boolean hotkeyShowHiddenItems() { return true; }
    @ConfigItem(
            keyName = "hotkeyDoubleTapTogglesOverlay",
            name = "Double-tap: toggle overlay",
            description = "When enabled, double-tap the hotkey to toggle the entire ground items overlay.",
            section = hotkey,
            position = 2
    )
    default boolean hotkeyDoubleTapTogglesOverlay() { return true; }
    @ConfigItem(
            keyName = "hotkeyDoubleTapDelay",
            name = "Double-tap delay",
            description = "Period within which to register a hotkey double-tap.",
            section = hotkey,
            position = 3
    )
    @Units(Units.MILLISECONDS)
    default int hotkeyDoubleTapDelay() { return 250; }

    @ConfigSection(
            name = "Display overrides",
            description = "Configure global display overrides.",
            position = 2
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
            description = "Configure default lists of highlighted and hidden items. Values are case-insensitive, separated by comma. These lists are checked AFTER the active filter, but before item value rules.",
            position = 8
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
    @ConfigItem(keyName = "highlightedItems", name = "", description = "")
    void setHighlightedItems(String key);
    @ConfigItem(keyName = "hiddenItems", name = "", description = "")
    void setHiddenItems(String key);

    @ConfigItem(
            keyName = "highlightColor",
            name = "Highlight color",
            description = "Configures the color for highlighted items.",
            section = itemLists,
            position = 2
    )
    default Color highlightColor() { return Color.decode("#aa00ff"); }
    @ConfigItem(
            keyName = "highlightLootbeam",
            name = "Highlight lootbeam",
            description = "Configures whether highlighted items show a lootbeam.",
            section = itemLists,
            position = 3
    )
    default boolean highlightLootbeam() { return false; }

    @ConfigSection(
            name = "Item value rules",
            description = "Configure default rules for showing based on item value. These rules are checked AFTER both the active filter and the global hide list.",
            position = 9
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
    @ConfigItem(
            keyName = "hiddenTierEnabled",
            name = "Hidden tier",
            description = "Hide items below a certain value.",
            section = itemValueRules,
            position = 13
    )
    default boolean hideTierEnabled() { return false; }
    @ConfigItem(
            keyName = "hiddenValue",
            name = "Hide below value",
            description = "Hide items below this value, if enabled.",
            section = itemValueRules,
            position = 14
    )
    default int hideTierValue() { return 0; }
}
