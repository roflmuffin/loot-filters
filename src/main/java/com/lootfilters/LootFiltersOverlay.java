package com.lootfilters;

import net.runelite.api.Client;
import net.runelite.api.TileItem;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.TextComponent;

import javax.inject.Inject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

import static net.runelite.api.Perspective.getCanvasTextLocation;
import static net.runelite.client.ui.FontManager.getRunescapeSmallFont;

public class LootFiltersOverlay extends Overlay {
    private static final int Z_STACK_OFFSET = 16; // for initial perspective and subsequent vertical stack
    private static final int BOX_PAD = 2;

    private final Client client;
    private final LootFiltersPlugin plugin;
    private final LootFiltersConfig config;

    @Inject
    private ItemManager itemManager;

    @Inject
    public LootFiltersOverlay(Client client, LootFiltersPlugin plugin, LootFiltersConfig config) {
        setPosition(OverlayPosition.DYNAMIC);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D g) {
        var filters = plugin.getFilterConfigs();
        for (var entry: plugin.getTileItemIndex().entrySet()) {
            var tile = entry.getKey();
            var currentOffset = 0;
            for (var item: entry.getValue()) {
                var match = filters.stream()
                        .filter(it -> it.test(plugin, item))
                        .findFirst().orElse(null);
                if (match == null || match.getDisplay().isHidden()) {
                    continue;
                }

                var display = match.getDisplay();
                var displayText = buildDisplayText(item, display);

                var loc = LocalPoint.fromWorld(client.getTopLevelWorldView(), tile.getWorldLocation());
                if (loc == null) {
                    continue;
                }

                if (tile.getItemLayer() == null) {
                    continue;
                }
                var textPoint = getCanvasTextLocation(client, g, loc, displayText, tile.getItemLayer().getHeight() + Z_STACK_OFFSET);
                if (textPoint == null) {
                    continue;
                }

                var fm = g.getFontMetrics(getRunescapeSmallFont());
                var textWidth = fm.stringWidth(displayText);
                var textHeight = fm.getHeight();

                var text = new TextComponent();
                text.setText(displayText);
                text.setFont(getRunescapeSmallFont());
                text.setColor(display.getTextColor());
                text.setPosition(new Point(textPoint.getX(), textPoint.getY() - currentOffset));

                if (display.getBackgroundColor() != null) {
                    g.setColor(display.getBackgroundColor());
                    g.fillRect(
                            textPoint.getX() - BOX_PAD,
                            textPoint.getY() - currentOffset - textHeight - BOX_PAD,
                            textWidth + 2*BOX_PAD,
                            textHeight + 2*BOX_PAD
                    );
                }
                if (display.getBorderColor() != null) {
                    g.setColor(display.getBorderColor());
                    g.drawRect(
                            textPoint.getX() - BOX_PAD,
                            textPoint.getY() - currentOffset - textHeight - BOX_PAD,
                            textWidth + 2*BOX_PAD,
                            textHeight + 2*BOX_PAD
                    );
                }

                text.render(g);

                if (display.isShowDespawn()) {
                    var ticksRemaining = item.getDespawnTime() - client.getTickCount();
                    if (ticksRemaining < 0) { // doesn't despawn
                        continue;
                    }
                    text.setColor(getDespawnTextColor(item));
                    text.setText(Integer.toString(ticksRemaining));
                    text.setPosition(new Point(textPoint.getX() + textWidth + 2 + 1, textPoint.getY() - currentOffset));
                    text.render(g);
                }

                currentOffset += Z_STACK_OFFSET;
            }
        }
        return null;
    }

    private Color getDespawnTextColor(TileItem item) {
        if (item.getDespawnTime() - client.getTickCount() < 100) {
            return Color.RED;
        }
        if (item.getVisibleTime() <= client.getTickCount()) {
            return Color.YELLOW;
        }
        return Color.GREEN;
    }

    private String buildDisplayText(TileItem item, DisplayConfig display) {
        var text = itemManager.getItemComposition(item.getId()).getName();
        if (item.getQuantity() > 1) {
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
        } else if (value >= 1e7) { // > 10m
            return String.format("%.1fM", (float)value / 1e6);
        } else if (value >= 1e6) { // > 1m
            return String.format("%.2fM", (float)value / 1e6);
        } else if (value >= 1e5) { // > 100k
            return String.format("%.0fK", (float)value / 1e3);
        } else if (value >= 1e4) { // > 10k
            return String.format("%.1fK", (float)value / 1e3);
        } else if (value >= 1e3) { // > 1k
            return String.format("%.2fK", (float)value / 1e3);
        }
        return value + "gp";
    }
}
