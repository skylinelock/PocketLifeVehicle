package dev.sky_lock.mocar.listener;

import dev.sky_lock.mocar.car.CraftCar;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author sky_lock
 */

public class EntityListener implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof CraftCar) {
            event.getDrops().clear();
            event.getDrops().add(new ItemStack(Material.WOOD_HOE));
        }
    }
}
