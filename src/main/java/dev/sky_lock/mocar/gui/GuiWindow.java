package dev.sky_lock.mocar.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

/**
 * @author sky_lock
 */

public class GuiWindow {
    private final Inventory inventory;

    public GuiWindow(String title) {
        inventory = Bukkit.createInventory(null, InventoryType.CHEST, title);
    }

    public void show(Player player) {
        player.openInventory(inventory);
    }
}
