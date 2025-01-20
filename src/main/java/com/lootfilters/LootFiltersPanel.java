package com.lootfilters;

import com.lootfilters.lang.CompileException;
import lombok.SneakyThrows;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;

import static com.lootfilters.util.CollectionUtil.append;
import static com.lootfilters.util.FilterUtil.configToFilterSource;
import static com.lootfilters.util.TextUtil.quote;
import static java.util.Collections.emptyList;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.SwingUtilities.invokeLater;
import static net.runelite.client.util.ImageUtil.loadImageResource;

public class LootFiltersPanel extends PluginPanel {
    private static final String NONE_ITEM = "<none>";
    private static final String NONE_TEXT = "Select a filter to display its source.";
    private static final Font TEXT_FONT_ACTIVE = new Font(Font.MONOSPACED, Font.PLAIN, 12);
    private static final Color TEXT_BG_ACTIVE = Color.decode("#1e1e1e");

    private final LootFiltersPlugin plugin;
    private final JComboBox<String> filterSelect;
    private final JTextArea filterText;
    private final JPanel root;

    public LootFiltersPanel(LootFiltersPlugin plugin) throws Exception {
        this.plugin = plugin;

        filterSelect = new JComboBox<>();
        filterText = new JTextArea(24, 30);
        root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));

        init();
        initFilterSelect();
    }

    private void init() {
        var top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        var bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));

        var label = new JLabel("Active filter:");
        var importClipboard = createIconButton("import_clipboard",
                "Import new filter from clipboard.",
                this::onImportClipboard);
        var importConfig = createIconButton("import_config",
                "Import item highlight and hide lists into a new filter. Doing this will also reset those lists.",
                this::onImportConfig);
        var deleteActive = createIconButton("delete_active",
                "Delete the currently active filter.",
                this::onDeleteActive);
        var deleteAll = new JButton("Delete all");
        deleteAll.addActionListener(it -> onDeleteAll());

        top.add(label);
        top.add(importClipboard);
        top.add(importConfig);
        top.add(deleteActive);
        bottom.add(deleteAll);
        bottom.add(new JScrollPane(filterText));

        root.add(top);
        root.add(filterSelect);
        root.add(bottom);

        add(root);
    }

    private void initFilterSelect() throws IOException {
        var filters = plugin.getUserFilters();
        filterSelect.addItem(NONE_ITEM);
        for (var filter : filters) {
            filterSelect.addItem(LootFilter.fromSource(filter).getName());
        }

        var index = plugin.getUserFilterIndex();
        if (index <= filters.size() - 1) {
            filterSelect.setSelectedIndex(index + 1);
        }

        filterSelect.addActionListener(this::onFilterSelect);

        filterText.setLineWrap(true);
        updateFilterText(index);
    }

    private void onImportClipboard() {
        var newSrc = getClipboard();
        if (newSrc == null) {
            plugin.addChatMessage("No text in clipboard.");
            return;
        }

        LootFilter newFilter;
        try {
            newFilter = LootFilter.fromSource(newSrc);
        } catch (CompileException | IOException e) {
            plugin.addChatMessage("Failed to load filter from clipboard: " + e.getMessage());
            return;
        }

        if (newFilter.getName() == null || newFilter.getName().isBlank()) {
            var newName = showInputDialog(this, "This filter does not have a name. Enter one:");
            if (newName == null || newName.isBlank()) {
                return;
            }
            newSrc = "meta { name = " + quote(newName) + "; }\n" + newSrc;
            newFilter.setName(newName);
        }

        if (tryUpdateExisting(newFilter.getName(), newSrc)) {
            return;
        }

        filterSelect.addItem(newFilter.getName());
        plugin.setUserFilters(append(plugin.getUserFilters(), newSrc));
    }

    @SneakyThrows
    private void onImportConfig() {
        var initialName = plugin.getClient().getLocalPlayer() != null
                ? plugin.getClient().getLocalPlayer().getName() + "/"
                : "player/";
        var finalName = showInputDialog(this, "Enter a filter name:", initialName);
        if (finalName == null) {
            return;
        }

        var src = configToFilterSource(plugin.getConfig(), finalName);
        var newFilter = LootFilter.fromSource(src); // not catching here because src is generated by plugin
        if (tryUpdateExisting(finalName, src)) {
            return;
        }

        filterSelect.addItem(newFilter.getName());
        plugin.setUserFilters(append(plugin.getUserFilters(), src));
        plugin.getConfig().setHighlightedItems("");
        plugin.getConfig().setHiddenItems("");
    }

    private void onFilterSelect(ActionEvent event) {
        var realIndex = filterSelect.getSelectedIndex() - 1;
        plugin.setUserFilterIndex(realIndex);
        updateFilterText(realIndex);
    }

    private void onDeleteActive() {
        var toDelete = filterSelect.getSelectedIndex() - 1;
        if (plugin.getUserFilters().isEmpty() || toDelete == -1) {
            return;
        }
        if (!confirm("Delete the active loot filter?")) {
            return;
        }

        var newCfg = new ArrayList<>(plugin.getUserFilters());
        newCfg.remove(toDelete);

        filterSelect.removeItemAt(toDelete + 1);
        filterSelect.setSelectedIndex(0);
        plugin.setUserFilters(newCfg);
        plugin.setUserFilterIndex(-1);
        updateFilterText(-1);
    }

    private void onDeleteAll() {
        if (!confirm("Delete all loot filters?")) { return; }
        if (!confirm("Are you sure?")) { return; }

        filterSelect.removeActionListener(this::onFilterSelect);
        filterSelect.removeAllItems();
        filterSelect.addItem(NONE_ITEM);
        filterSelect.setSelectedIndex(0);
        plugin.setUserFilters(emptyList());
        plugin.setUserFilterIndex(-1);
        updateFilterText(-1);
        invokeLater(() -> filterSelect.addActionListener(this::onFilterSelect));
    }

    private boolean tryUpdateExisting(String newName, String newSrc) {
        var existing = plugin.getUserFilters();
        for (int i = 0; i < filterSelect.getItemCount(); ++i) {
            if (!filterSelect.getItemAt(i).equals(newName)) {
                continue;
            }
            if (!confirm("Filter " + quote(newName) + " already exists. Update it?")) {
                return true;
            }

            existing.set(i, newSrc);
            plugin.setUserFilters(existing);
            return true;
        }
        return false;
    }

    private void updateFilterText(int index) {
        if (index > -1) {
            filterText.setText(plugin.getUserFilters().get(index));
            filterText.setEnabled(true);
            filterText.setEditable(false);
            filterText.setBackground(TEXT_BG_ACTIVE);
            filterText.setFont(TEXT_FONT_ACTIVE);
        } else {
            filterText.setText(NONE_TEXT);
            filterText.setEnabled(false);
            filterText.setBackground(null);
            filterText.setFont(FontManager.getRunescapeFont());
        }
        filterText.setCaretPosition(0);
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

    private static String getClipboard() {
        try {
            return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (Exception e) {
            return null;
        }
    }

    private static ImageIcon icon(String name) {
        var img = loadImageResource(LootFiltersPanel.class, "/com/lootfilters/icons/" + name + ".png");
        return new ImageIcon(img);
    }
}
