package dev.sky_lock.mocar.gui.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.*;
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
    private final GuiType type;

    public GuiWindow(String title, Player holder, GuiType type) {
        this.holder = holder;
        this.type = type;
        windows.add(this);
        this.inventory = createGui(holder, type, title);
    }

    private Inventory createGui(Player holder, GuiType type, String title) {
        switch (type) {
            case ANVIL:
                return Bukkit.createInventory(holder, InventoryType.ANVIL, title);
            case SMALL:
                return Bukkit.createInventory(holder, 27, title);
            case BIG:
                return Bukkit.createInventory(holder, 54, title);
        }
        return Bukkit.createInventory(holder, 54, title);
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
        windows.stream().filter(window -> window.getInventory().equals(event.getInventory())).forEach(window -> {
            if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
                event.setResult(Event.Result.DENY);
                event.setCancelled(true);
            }
        });
        windows.stream().filter(window -> window.getInventory().equals(event.getClickedInventory())).findFirst().ifPresent(window -> {
            IGuiComponent component = window.getComponentAtSlot(event.getSlot());
            if (component == null) {
                if (window.getInventory().getType() == event.getClickedInventory().getType()) {
                    InventoryAction action = event.getAction();
                    if (action != InventoryAction.PLACE_ALL && action != InventoryAction.PLACE_ONE && action != InventoryAction.PLACE_SOME) {
                        return;
                    }
                    event.setCancelled(true);
                }
                return;
            }
            if (!event.isCancelled()) {
                event.setResult(Event.Result.DENY);
                event.setCancelled(true);
            }
            component.onClick(event);
        });
    }

    public static void drag(InventoryDragEvent event) {
        windows.stream().filter(window -> window.getInventory().getName().equals(event.getInventory().getName())).findFirst().ifPresent(window -> {
            event.setCancelled(true);
            //TODO: 要修正
        });
    }

    public static void close(InventoryCloseEvent event) {
        windows.removeIf(window -> {
            if (window.getInventory().equals(event.getInventory())) {
                window.close((Player) event.getPlayer());
                return true;
            }
            return false;
        });
    }
}
