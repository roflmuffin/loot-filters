package com.lootfilters;

import net.runelite.api.Client;
import net.runelite.api.TileItem;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.components.TextComponent;

import javax.inject.Inject;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

import static net.runelite.api.Perspective.getCanvasTextLocation;
import static net.runelite.api.coords.LocalPoint.fromWorld;
import static net.runelite.client.ui.FontManager.getRunescapeSmallFont;

public class LootFiltersOverlay extends Overlay {
    private final Client client;
    private final LootFiltersPlugin plugin;
    private final LootFiltersConfig config;

    @Inject
    private ItemManager itemManager;

    @Inject
    public LootFiltersOverlay(Client client, LootFiltersPlugin plugin, LootFiltersConfig config) {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D g) {
        var filters = plugin.getFilterConfigs();
        for (var entry: plugin.getTileItemIndex().entrySet()) {
            var tile = entry.getKey();
            var offset = 0;
            for (var item: entry.getValue()) {
                var match = filters.stream()
                        .filter(it -> it.test(plugin, item))
                        .findFirst().orElse(null);
                if (match == null || match.getDisplay().isHidden()) {
                    continue;
                }

                var display = match.getDisplay();
                var displayText = buildDisplayText(item, display);

                var loc = fromWorld(client, tile.getWorldLocation());
                if (loc == null) {
                    continue;
                }

                if (tile.getItemLayer() == null) {
                    continue;
                }
                var textPoint = getCanvasTextLocation(client, g, loc, displayText, tile.getItemLayer().getHeight());
                if (textPoint == null) {
                    continue;
                }

                offset += 16; // configurize this
                var text = new TextComponent();
                text.setText(displayText);
                text.setFont(getRunescapeSmallFont());
                text.setColor(display.getColor());
                text.setPosition(new Point(textPoint.getX(), textPoint.getY() - offset));
                text.render(g);
            }
        }
        return null;
    }

    private String buildDisplayText(TileItem item, DisplayConfig display) {
        var text = itemManager.getItemComposition(item.getId()).getName();
        if (display.isShowQuantity()) {
            text += " (" + item.getQuantity() + ")";
        }
        if (display.isShowValue()) {
            var value = itemManager.getItemPrice(item.getId()) * item.getQuantity();
            text += " (" + getValueText(value) + ")";
        }
        return text;
    }

    private String getValueText(int value) {
        if (value >= 1e9) { // > 1b
            return String.format("%.2fB", (float)value / 1e9);
        } else if (value >= 1e8) { // > 100m
            return String.format("%.0fM", (float)value / 1e6);
        } else if (value >= 1e6) { // > 1m
            return String.format("%.1fM", (float)value / 1e6);
        } else if (value >= 1e5) { // > 100k
            return String.format("%.0fK", (float)value / 1e3);
        }
        return value + "gp";
    }
}
