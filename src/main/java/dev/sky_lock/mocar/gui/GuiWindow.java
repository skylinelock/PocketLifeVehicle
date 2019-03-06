package dev.sky_lock.mocar.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sky_lock
 */

public class GuiWindow {
    private final Inventory inventory;
    private final Player player;
    private final List<GuiItem> items = new ArrayList<>();

    public GuiWindow(String title, Player player) {
        this.player = player;
        inventory = Bukkit.createInventory(player, 54, title);
    }

    public void open() {
        player.openInventory(inventory);
    }

    public void addGuiItem(GuiItem item) {
        inventory.setItem(item.getSlot(), item.getItemStack());
        items.add(item);
    }



}
