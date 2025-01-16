package com.lootfilters;

import net.runelite.api.ChatMessageType;
import net.runelite.client.ui.PluginPanel;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.util.ArrayList;

import static com.lootfilters.util.CollectionUtil.append;
import static com.lootfilters.util.FilterUtil.configToFilterSource;
import static com.lootfilters.util.TextUtil.quote;
import static java.util.Collections.emptyList;
import static javax.swing.JOptionPane.showConfirmDialog;
import static net.runelite.client.util.ImageUtil.loadImageResource;
import static net.runelite.client.util.ImageUtil.resizeImage;

public class LootFiltersPanel extends PluginPanel {
    private final LootFiltersPlugin plugin;
    private final JComboBox<String> filterSelect;
    private final JPanel root;

    public LootFiltersPanel(LootFiltersPlugin plugin) throws Exception {
        this.plugin = plugin;

        filterSelect = new JComboBox<>();
        root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));

        init();
        initFilterSelect();
    }

    private void init() {
        var top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        var bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));

        var label = new JLabel("Active filter:");
        var importClipboard = createIconButton("add_icon",
                "Import new filter from clipboard.",
                this::onImportClipboard);
        var importConfig = createIconButton("config_icon_2",
                "Import highlight, hide, and item value config settings into a new filter.",
                this::onImportConfig);
        var deleteActive = createIconButton("delete_icon",
                "Delete the currently active filter.",
                this::onDeleteActive);
        var deleteAll = new JButton("Delete all");
        deleteAll.addActionListener(it -> onDeleteAll());

        top.add(label);
        top.add(importClipboard);
        top.add(importConfig);
        top.add(deleteActive);
        bottom.add(deleteAll);

        root.add(top);
        root.add(filterSelect);
        root.add(bottom);

        add(root);
    }

    private void initFilterSelect() throws IOException {
        var filters = plugin.getUserFilters();
        for (var filter : filters) {
            filterSelect.addItem(LootFilter.fromSource(filter).getName());
        }
        var index = plugin.getUserFilterIndex();
        if (index <= filters.size() - 1) {
            filterSelect.setSelectedIndex(index);
        }

        filterSelect.addActionListener(it -> onFilterSelect());
    }

    private void onImportClipboard() {
        String source;
        try {
            source = getClipboard();
        } catch (Exception e) {
            return;
        }

        LootFilter newFilter;
        try {
            newFilter = LootFilter.fromSource(source);
        } catch (Exception e) {
            plugin.getClientThread().invoke(() -> {
                plugin.getClient().addChatMessage(ChatMessageType.GAMEMESSAGE, "",
                        "Failed to load filter from clipboard: " + e.getMessage(), "");
            });
            return;
        }

        for (int i = 0; i < filterSelect.getItemCount(); ++i) {
            if (filterSelect.getItemAt(i).equals(newFilter.getName())) {
                plugin.getClientThread().invoke(() -> {
                    plugin.getClient().addChatMessage(ChatMessageType.GAMEMESSAGE, "",
                            "Cannot import filter: a filter with name " + newFilter.getName() + " already exists", "");
                });
                return;
            }
        }

        filterSelect.addItem(newFilter.getName());

        var newCfg = new ArrayList<>(plugin.getUserFilters());
        newCfg.add(source);
        plugin.setUserFilters(newCfg);
    }

    private void onImportConfig() {
        var initialName = plugin.getClient().getLocalPlayer() != null
                ? plugin.getClient().getLocalPlayer().getName() + "/"
                : "player/";
        var finalName = JOptionPane.showInputDialog(this, "Enter a filter name:", initialName);
        if (finalName == null) {
            return;
        }

        var src = configToFilterSource(plugin.getConfig(), finalName);
        LootFilter newFilter;
        try {
            newFilter = LootFilter.fromSource(src);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        filterSelect.addItem(newFilter.getName());
        plugin.setUserFilters(append(plugin.getUserFilters(), src));
    }

    private void onFilterSelect() {
        var newIndex = filterSelect.getSelectedIndex();
        if (newIndex == -1) {
            return;
        }

        plugin.setUserFilterIndex(newIndex);
        plugin.addPluginChatMessage("Loading filter " + quote((String) filterSelect.getSelectedItem()));
    }

    private void onDeleteActive() {
        var index = filterSelect.getSelectedIndex();
        if (plugin.getUserFilters().isEmpty() || index == -1) {
            return;
        }
        if (!confirm("Delete the active loot filter?")) {
            return;
        }

        var newCfg = new ArrayList<>(plugin.getUserFilters());
        newCfg.remove(index);

        filterSelect.removeItemAt(index);
        filterSelect.setSelectedIndex(-1);
        plugin.setUserFilters(newCfg);
        plugin.setUserFilterIndex(-1);
    }

    private void onDeleteAll() {
        if (!confirm("Delete all loot filters?")) { return; }
        if (!confirm("Are you sure?")) { return; }

        filterSelect.removeAllItems();
        filterSelect.setSelectedIndex(-1);
        plugin.setUserFilters(emptyList());
        plugin.setUserFilterIndex(-1);
    }

    private JButton createIconButton(String iconSource, String tooltip, Runnable onClick) {
        var button = new JButton("", icon(iconSource));
        button.setToolTipText(tooltip);
        button.setBackground(null);
        button.setBorder(null);
        button.addActionListener(it -> onClick.run());
        return button;
    }

    private boolean confirm(String confirmText) {
        var result = showConfirmDialog(this, confirmText, "Confirm", JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }

    private static String getClipboard() throws Exception {
        return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
    }

    private static ImageIcon icon(String name) {
        var img = loadImageResource(LootFiltersPanel.class, "/com/lootfilters/icons/" + name + ".png");
        img = resizeImage(img, 18, 18);
        return new ImageIcon(img);
    }
}
