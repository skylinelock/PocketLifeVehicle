package dev.sky_lock.mocar.gui.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author sky_lock
 */

public class GuiWindow {
    private static final List<GuiWindow> windows = new ArrayList<>();
    private final List<UUID> opening = new ArrayList<>();
    private final Inventory inventory;
    private final Player holder;
    private final List<IGuiComponent> components = new ArrayList<>();

    public GuiWindow(String title, Player holder) {
        this.holder = holder;
        windows.add(this);
        inventory = Bukkit.createInventory(holder, 54, title);
    }

    public void open(Player player) {
        if (!player.getUniqueId().equals(holder.getUniqueId())) {
            opening.add(player.getUniqueId());
        }
        player.openInventory(inventory);
    }

    public void close(Player player) {
        if (!player.getUniqueId().equals(holder.getUniqueId())) {
            opening.remove(holder.getUniqueId());
        }
        player.closeInventory();
    }

    protected void addComponent(IGuiComponent component) {
        components.add(component);
        component.getSlotRange().forEach((integer -> {
            inventory.setItem(integer, component.getItemStack());
        }));
    }

    private IGuiComponent getComponentAtSlot(int slot) {
        return components.stream().filter(component -> component.getSlotRange().contains(slot)).findFirst().orElse(null);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public static void click(InventoryClickEvent event) {
        windows.stream().filter(window -> window.getInventory().getName().equals(event.getInventory().getName())).findFirst().ifPresent(window -> {
            IGuiComponent component = window.getComponentAtSlot(event.getSlot());
            if (component == null) {
                return;
            }
            if (!event.isCancelled()) {
                event.setResult(Event.Result.DENY);
                event.setCancelled(true);
            }
            component.onClick(event);
        });
    }
}
