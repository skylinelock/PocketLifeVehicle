package dev.sky_lock.mocar.listener;

import dev.sky_lock.mocar.car.CraftCar;
import dev.sky_lock.mocar.gui.CarUtilityGui;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

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
        if (!(as instanceof CraftCar)) {
            return;
        }
        event.setCancelled(true);
        Player player = event.getPlayer();

        if (player.isSneaking()) {
            CarUtilityGui carUtil = new CarUtilityGui(player);
        } else {
            as.addPassenger(event.getPlayer());
        }
    }

}
