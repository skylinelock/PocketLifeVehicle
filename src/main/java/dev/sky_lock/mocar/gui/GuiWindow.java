package dev.sky_lock.mocar.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sky_lock
 */

public class GuiWindow {
    private final Inventory inventory;
    private final Player player;
    private final List<IGuiComponent> components = new ArrayList<>();

    public GuiWindow(String title, Player player) {
        this.player = player;
        inventory = Bukkit.createInventory(player, 54, title);
    }

    public void open() {
        player.openInventory(inventory);
    }

    protected void addComponent(IGuiComponent component) {
        components.add(component);
        component.getSlotRange().forEach((integer -> {
            inventory.setItem(integer, component.getItemStack());
        }));
    }

    public void click(InventoryClickEvent event) {
        components.stream().filter(component -> component.getSlotRange().contains(event.getSlot())).findFirst().ifPresent(component -> component.onClick(event));
    }

}
