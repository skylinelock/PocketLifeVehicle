package dev.sky_lock.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;

/**
 * @author sky_lock
 */

public class InventoryMenuListener implements Listener {
    private final JavaPlugin plugin;

    public InventoryMenuListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder == null) {
            return;
        }
        if (!(holder instanceof InventoryMenu)) {
            return;
        }
        if (event.getSlotType() == InventoryType.SlotType.OUTSIDE) {
            return;
        }
        if (event.getClickedInventory().equals(event.getView().getBottomInventory())) {
            if (event.getClick().isShiftClick()) {
                event.setCancelled(true);
            }
            return;
        }
        if (!event.getInventory().equals(event.getClickedInventory())) {
            return;
        }
        if (event.getAction() == InventoryAction.HOTBAR_SWAP || event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD) {
            event.setCancelled(true);
            return;
        }
        if (!event.getClick().isRightClick() && !event.getClick().isLeftClick()) {
            event.setCancelled(true);
            return;
        }
        event.setCancelled(true);
        ((InventoryMenu) holder).click(event);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        InventoryMenu.of((Player) event.getWhoClicked()).ifPresent(menu -> {
            if (event.getInventory().equals(event.getView().getBottomInventory())) {
                if (event.getType() == DragType.SINGLE) {
                    return;
                }
            }
            event.setCancelled(true);
        });
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Optional<InventoryMenu> menu = InventoryMenu.of((Player) event.getPlayer());
        new BukkitRunnable() {
            @Override
            public void run() {
                menu.ifPresent(menu -> {
                    menu.remove((Player) event.getPlayer());
                });
            }
        }.runTaskLater(plugin, 1L);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        InventoryMenu.of(event.getPlayer()).ifPresent(menu -> menu.close(event.getPlayer()));
    }

}
