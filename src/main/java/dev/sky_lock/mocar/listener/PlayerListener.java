package dev.sky_lock.mocar.listener;

import dev.sky_lock.mocar.car.CarEntities;
import dev.sky_lock.mocar.car.CarModel;
import dev.sky_lock.mocar.car.CraftCar;
import dev.sky_lock.mocar.car.ModelList;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author sky_lock
 */

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack itemStack = event.getItem();
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (event.getBlockFace() != BlockFace.UP){
            return;
        }
        CarModel model = ModelList.getModel(itemStack);
        if (model == null) {
            return;
        }
        event.setCancelled(true);
        event.setUseInteractedBlock(Event.Result.DENY);
        event.setUseItemInHand(Event.Result.DENY);
        event.getPlayer().getInventory().remove(itemStack);
        Location whereToSpawn = event.getClickedBlock().getLocation();
        whereToSpawn.add(0, 1.0, 0);
        CarEntities.spawn(event.getPlayer().getUniqueId(), model, whereToSpawn);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Entity vehicle = player.getVehicle();
        if (vehicle == null) {
            return;
        }
        if (!(vehicle instanceof CraftCar)) {
            return;
        }
        CraftCar craftCar = (CraftCar) vehicle;
        craftCar.removePassenger(player);
    }
}
