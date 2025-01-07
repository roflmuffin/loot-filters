package com.lootfilters;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class LootFiltersPluginTest {
	public static void main(String[] args) throws Exception {
		ExternalPluginManager.loadBuiltin(LootFiltersPlugin.class);
		RuneLite.main(args);
	}
}