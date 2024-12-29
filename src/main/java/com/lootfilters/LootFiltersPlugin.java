package com.lootfilters;

import com.google.inject.Provides;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Tile;
import net.runelite.api.TileItem;
import net.runelite.api.events.ItemDespawned;
import net.runelite.api.events.ItemSpawned;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@PluginDescriptor(
	name = "Loot Filters"
)
@Getter
public class LootFiltersPlugin extends Plugin
{
	@Inject private Client client;
	@Inject private ClientThread clientThread;
	@Inject private LootFiltersConfig config;
	@Inject private LootFiltersOverlay overlay;
	@Inject private OverlayManager overlayManager;
	@Inject private ConfigManager configManager;
	@Inject private ItemManager itemManager;

	private final Map<Tile, List<TileItem>> groundItems = new HashMap<>();
	private final LootbeamIndex lootbeamIndex = new LootbeamIndex();

	private List<FilterConfig> filterConfigs;

	@Override
	protected void startUp() throws Exception {
		overlayManager.add(overlay);

		filterConfigs = FilterConfig.fromJson(config.filterConfig());
	}

	@Override
	protected void shutDown() throws Exception {
		overlayManager.remove(overlay);
		groundItems.clear();
		lootbeamIndex.clear();
	}

	@Provides
	LootFiltersConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(LootFiltersConfig.class);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event) {
		if (event.getGroup().equals("loot-filters")) {
			filterConfigs = FilterConfig.fromJson(config.filterConfig());
		}
	}

	@Subscribe
	public void onItemSpawned(ItemSpawned event) {
		var tile = event.getTile();
		var item = event.getItem();
		if (!groundItems.containsKey(tile)) {
			groundItems.put(tile, new ArrayList<>());
		}
		groundItems.get(tile).add(item);

		// lootbeams
		var match = filterConfigs.stream()
				.filter(it -> it.test(this, item))
				.findFirst().orElse(null);
		if (match == null || !match.getDisplay().isShowLootbeam()) {
			return;
		}

		var beam = new Lootbeam(client, clientThread, tile.getWorldLocation(), match.getDisplay().getColor(), Lootbeam.Style.MODERN);
		lootbeamIndex.put(tile, item, beam);
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

		lootbeamIndex.remove(tile, item); // idempotent, we don't care if there wasn't a beam
	}
}
