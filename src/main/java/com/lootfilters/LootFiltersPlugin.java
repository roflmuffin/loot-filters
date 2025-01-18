package com.lootfilters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Provides;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemDespawned;
import net.runelite.api.events.ItemSpawned;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOpened;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.input.MouseManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.lootfilters.util.FilterUtil.withConfigMatchers;
import static com.lootfilters.util.TextUtil.quote;
import static java.util.Collections.emptyList;
import static net.runelite.client.util.ImageUtil.loadImageResource;

@Slf4j
@PluginDescriptor(
	name = "Loot Filters"
)
@Getter
public class LootFiltersPlugin extends Plugin {
	public static final String CONFIG_GROUP = "loot-filters";
	public static final String USER_FILTERS_KEY = "user-filters";
	public static final String USER_FILTERS_INDEX_KEY = "user-filters-index";

	@Inject private Client client;
	@Inject private ClientToolbar clientToolbar;
	@Inject private ClientThread clientThread;

	@Inject private LootFiltersConfig config;
	@Inject private LootFiltersOverlay overlay;
	@Inject private LootFiltersMouseAdapter mouseAdapter;

	@Inject private OverlayManager overlayManager;
	@Inject private MouseManager mouseManager;
	@Inject private ConfigManager configManager;
	@Inject private ItemManager itemManager;

	private LootFiltersPanel pluginPanel;
	private NavigationButton pluginPanelNav;

	private final TileItemIndex tileItemIndex = new TileItemIndex();
	private final LootbeamIndex lootbeamIndex = new LootbeamIndex(this);
	private final MenuEntryComposer menuEntryComposer = new MenuEntryComposer(this);

	private LootFilter activeFilter;
	private LootFilter currentAreaFilter;
	private List<LootFilter> parsedUserFilters;

	@Setter private int hoveredItem = -1;

	public LootFilter getActiveFilter() {
		return currentAreaFilter != null ? currentAreaFilter : activeFilter;
	}

	public List<String> getUserFilters() {
		var cfg = configManager.getConfiguration(CONFIG_GROUP, USER_FILTERS_KEY);
		if (cfg == null || cfg.isEmpty()) {
			return emptyList();
		}

		var type = new TypeToken<List<String>>(){}.getType();
        return new Gson().fromJson(configManager.getConfiguration(CONFIG_GROUP, USER_FILTERS_KEY), type);
	}

	public void setUserFilters(List<String> filters) {
		parsedUserFilters = new ArrayList<>();
		for (var filter : filters) {
			try {
				parsedUserFilters.add(LootFilter.fromSource(filter));
			} catch (Exception e) {
				// pass - shouldn't occur, filters are vetted before this point
			}
		}

		var json = new Gson().toJson(filters);
		configManager.setConfiguration(CONFIG_GROUP, USER_FILTERS_KEY, json);
	}

	public int getUserFilterIndex() {
		var indexCfg = configManager.getConfiguration(CONFIG_GROUP, USER_FILTERS_INDEX_KEY);
        return indexCfg == null || indexCfg.isEmpty() ? -1 : Integer.parseInt(indexCfg);
	}

	public void setUserFilterIndex(int index) {
		configManager.setConfiguration(CONFIG_GROUP, USER_FILTERS_INDEX_KEY, Integer.toString(index));
	}

	public String getUserActiveFilter() {
		var filters = getUserFilters();
		var index = getUserFilterIndex();
		return filters.isEmpty() || index == -1 || index > filters.size()-1
				? "" : filters.get(index);
	}

	public void addChatMessage(String msg) {
		clientThread.invoke(() -> {
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", msg, "loot-filters", false);
		});
	}

	public String getItemName(int id) {
		return itemManager.getItemComposition(id).getName();
	}

	@Override
	protected void startUp() throws Exception {
		overlayManager.add(overlay);

		loadFilter();
		setUserFilters(getUserFilters()); // round-trip on startup to parse everything into memory

		pluginPanel = new LootFiltersPanel(this);
		pluginPanelNav = NavigationButton.builder()
				.tooltip("Loot filters")
				.icon(loadImageResource(this.getClass(), "/com/lootfilters/icons/panel_icon.png"))
				.panel(pluginPanel)
				.build();
		clientToolbar.addNavigation(pluginPanelNav);
		mouseManager.registerMouseListener(mouseAdapter);
	}

	@Override
	protected void shutDown() {
		overlayManager.remove(overlay);

		tileItemIndex.clear();
		lootbeamIndex.clear();

		clientToolbar.removeNavigation(pluginPanelNav);
		mouseManager.unregisterMouseListener(mouseAdapter);
	}

	@Provides
	LootFiltersConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(LootFiltersConfig.class);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event) throws Exception {
		if (!event.getGroup().equals(CONFIG_GROUP)) {
			return;
		}

		loadFilter();
		if (!config.autoToggleFilters()) {
			currentAreaFilter = null;
		} // if we're transitioning TO enabled, do nothing - onGameTick() will handle it
		clientThread.invoke(lootbeamIndex::reset);
	}

	@Subscribe
	public void onItemSpawned(ItemSpawned event) {
		var tile = event.getTile();
		var item = event.getItem();
		tileItemIndex.put(tile, item);

		// lootbeams
		var match = getActiveFilter().findMatch(this, item);
		if (match == null || !match.isShowLootbeam()) {
			return;
		}

		var beam = new Lootbeam(client, clientThread, tile.getWorldLocation(), match.getTextColor(), Lootbeam.Style.MODERN);
		lootbeamIndex.put(tile, item, beam);
	}

	@Subscribe
	public void onItemDespawned(ItemDespawned event) {
		var tile = event.getTile();
		var item = event.getItem();
		tileItemIndex.remove(tile, item);
		lootbeamIndex.remove(tile, item); // idempotent, we don't care if there wasn't a beam
	}

	@Subscribe
	public void onGameTick(GameTick event) {
		scanAreaFilter();
	}

	@Subscribe
	private void onMenuEntryAdded(MenuEntryAdded event) {
		menuEntryComposer.onMenuEntryAdded(event.getMenuEntry());
	}

	@Subscribe
	public void onMenuOpened(MenuOpened event) {
		menuEntryComposer.onMenuOpened();
	}

	private void loadFilter() throws Exception {
		var userFilter = LootFilter.fromSource(getUserActiveFilter());
		activeFilter = withConfigMatchers(userFilter, config);
	}

	private void scanAreaFilter() {
		if (!config.autoToggleFilters()) {
			return;
		}

		var player = client.getLocalPlayer();
		if (player == null) {
			return;
		}

		var p = WorldPoint.fromLocalInstance(client, player.getLocalLocation());
		var match = parsedUserFilters.stream()
				.filter(it -> it.isInActivationArea(p))
				.findFirst().orElse(null);
		if (match != null && (currentAreaFilter == null || !Objects.equals(match.getName(), currentAreaFilter.getName()))) {
			addChatMessage("Entering area for filter " + quote(match.getName()));
			currentAreaFilter = withConfigMatchers(match, config);
		} else if (match == null && currentAreaFilter != null) {
			addChatMessage("Leaving area for filter " + quote(currentAreaFilter.getName()));
			currentAreaFilter = null;
		}
	}
}
