package dev.sky_lock.mocar.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.Inventory;

/**
 * @author sky_lock
 */

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked().getType() != EntityType.ARMOR_STAND) {
            return;
        }
        ArmorStand as = (ArmorStand) event.getRightClicked();
        if (!as.hasMetadata("mocar-as")) {
            return;
        }
        event.setCancelled(true);
        Player player = event.getPlayer();

        if (player.isSneaking()) {
            Inventory inventory = Bukkit.createInventory(player, 54, "車両設定");
            player.openInventory(inventory);
        } else {
            as.addPassenger(event.getPlayer());
        }
    }

}
