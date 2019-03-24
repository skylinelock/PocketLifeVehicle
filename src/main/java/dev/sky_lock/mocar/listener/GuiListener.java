package dev.sky_lock.mocar.listener;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.CraftCar;
import dev.sky_lock.mocar.click.CarClick;
import dev.sky_lock.mocar.click.InventoryClick;
import dev.sky_lock.mocar.gui.EditSessions;
import dev.sky_lock.mocar.gui.SignEditor;
import dev.sky_lock.mocar.gui.StringEditor;
import dev.sky_lock.mocar.gui.api.GuiWindow;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author sky_lock
 */

public class GuiListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked().getType() != EntityType.ARMOR_STAND) {
            return;
        }
        ArmorStand as = (ArmorStand) event.getRightClicked();
        if (!(as instanceof CraftCar)) {
            return;
        }
        event.setCancelled(true);
        new CarClick(event.getPlayer(), (CraftCar) as).accept();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        new InventoryClick(event).accept();
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        SignEditor.close(player);
        StringEditor.close(player);
        GuiWindow.close(event);
        Bukkit.getScheduler().runTaskLater(MoCar.getInstance(), () -> {
            if (player.getOpenInventory().getTopInventory().getType() == InventoryType.CRAFTING) {
                EditSessions.destroy(player.getUniqueId());
            }
        }, 1L);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        SignEditor.close(event.getPlayer());
        StringEditor.close(event.getPlayer());
        EditSessions.destroy(event.getPlayer().getUniqueId());
    }
}
