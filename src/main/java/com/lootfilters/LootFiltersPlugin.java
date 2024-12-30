package com.lootfilters;

import com.google.inject.Provides;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
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
import java.util.List;

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

	private final TileItemIndex tileItemIndex = new TileItemIndex();
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

		tileItemIndex.clear();
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
		tileItemIndex.put(tile, item);

		// lootbeams
		var match = filterConfigs.stream()
				.filter(it -> it.test(this, item))
				.findFirst().orElse(null);
		if (match == null || !match.getDisplay().isShowLootbeam()) {
			return;
		}

		var beam = new Lootbeam(client, clientThread, tile.getWorldLocation(), match.getDisplay().getTextColor(), Lootbeam.Style.MODERN);
		lootbeamIndex.put(tile, item, beam);
	}

	@Subscribe
	public void onItemDespawned(ItemDespawned event) {
		var tile = event.getTile();
		var item = event.getItem();
		tileItemIndex.remove(tile, item);
		lootbeamIndex.remove(tile, item); // idempotent, we don't care if there wasn't a beam
	}
}
