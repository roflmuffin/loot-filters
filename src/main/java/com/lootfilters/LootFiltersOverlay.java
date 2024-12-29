package com.lootfilters;

import net.runelite.api.Client;
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
        for (var entry: plugin.getGroundItems().entrySet()) {
            var tile = entry.getKey();
            var offset = 0;
            for (var item: entry.getValue()) {
                var match = filters.stream()
                        .filter(it -> it.test(plugin, item))
                        .findFirst().orElse(null);
                if (match == null) {
                    continue;
                }

                var display = match.getDisplay();
                var name = itemManager.getItemComposition(item.getId()).getName();

                var loc = fromWorld(client, tile.getWorldLocation());
                var textPoint = getCanvasTextLocation(client, g, loc, name, tile.getItemLayer().getHeight());
                if (textPoint == null) {
                    continue;
                }

                offset += 20; // configurize this
                var text = new TextComponent();
                text.setText(name);
                text.setFont(getRunescapeSmallFont());
                text.setColor(display.getColor());
                text.setPosition(new Point(textPoint.getX(), textPoint.getY() - offset));
                text.render(g);
            }
        }
        return null;
    }
}
