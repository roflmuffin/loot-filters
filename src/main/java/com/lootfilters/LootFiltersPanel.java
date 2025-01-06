package com.lootfilters;

import net.runelite.api.ChatMessageType;
import net.runelite.client.ui.PluginPanel;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;

import static net.runelite.client.util.ImageUtil.loadImageResource;

public class LootFiltersPanel extends PluginPanel {
    private final LootFiltersPlugin plugin;

    public LootFiltersPanel(LootFiltersPlugin plugin) {
        this.plugin = plugin;
        render();
    }

    private void render() {
        var placeholder = loadImageResource(this.getClass(), "/com/lootfilters/icons/Placeholder.png");
        var main = new JPanel();

        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

        var top = new JPanel();
        top.setLayout(new FlowLayout(FlowLayout.LEFT));

        var label = new JLabel("Active filter:");
        top.add(label);

        var addButton = new JButton("", new ImageIcon(placeholder));
        addButton.setBorder(null);
        top.add(addButton);
        var deleteButton = new JButton("", new ImageIcon(placeholder));
        addButton.setBorder(null);
        top.add(deleteButton);

        main.add(top);

        var filterSelect = new JComboBox<String>();
        filterSelect.addItem("shit");
        filterSelect.addItem("piss");
        filterSelect.addActionListener(it -> {
            plugin.getClientThread().invoke(() -> {
                plugin.getClient().addChatMessage(ChatMessageType.GAMEMESSAGE, "", (String) filterSelect.getSelectedItem(), "");
            });
        });
        main.add(filterSelect);

        add(main);
    }
}
