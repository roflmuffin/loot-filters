package com.lootfilters;

import com.google.inject.Provides;
import javax.inject.Inject;

import com.lootfilters.rule.ItemNameRule;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ItemDespawned;
import net.runelite.api.events.ItemSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.awt.*;
import java.util.*;
import java.util.List;

@Slf4j
@PluginDescriptor(
	name = "Loot Filters"
)
public class LootFiltersPlugin extends Plugin
{
	@Inject private Client client;
	@Inject private LootFiltersConfig config;
	@Inject private LootFiltersOverlay overlay;
	@Inject private OverlayManager overlayManager;
	@Inject private ConfigManager configManager;
	@Inject @Getter private ItemManager itemManager;

	private final Map<Tile, List<TileItem>> groundItems;

	private List<Filter> filters;

	public Map<Tile, List<TileItem>> getGroundItems() {
		return groundItems;
	}

	public List<Filter> getFilters() {
		return filters;
	}

	public LootFiltersPlugin() {
		this.groundItems = new HashMap<>();
	}

	@Override
	protected void startUp() throws Exception {
		overlayManager.add(overlay);

		this.filters = List.of( // testing
				new Filter(new ItemNameRule(this, "Torva platebody"), new DisplayConfig(Color.CYAN, false)),
				new Filter(new ItemNameRule(this, "Torva platelegs"), new DisplayConfig(Color.PINK, false))
		);
	}

	@Override
	protected void shutDown() throws Exception {
		overlayManager.remove(overlay);
	}

	@Provides
	LootFiltersConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(LootFiltersConfig.class);
	}

	@Subscribe
	public void onItemSpawned(ItemSpawned event) {
		var tile = event.getTile();
		var item = event.getItem();
		if (!groundItems.containsKey(tile)) {
			groundItems.put(tile, new ArrayList<>());
		}
		groundItems.get(tile).add(item);
	}

	@Subscribe
	public void onItemDespawned(ItemDespawned event) {
		var tile = event.getTile();
		if (!groundItems.containsKey(tile)) {
			return; // what?
		}

		var item = event.getItem();
		var items = groundItems.get(tile);
		items.remove(item);
		if (items.isEmpty()) {
			groundItems.remove(tile);
		}
	}
}
