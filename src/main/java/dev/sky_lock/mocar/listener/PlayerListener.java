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
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * @author sky_lock
 */

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (event.getBlockFace() != BlockFace.UP){
            return;
        }
        ItemStack itemStack = event.getItem();
        if (itemStack == null) {
            return;
        }
        CarModel model = ModelList.get(itemStack);
        if (model == null) {
            return;
        }
        if (!itemStack.hasItemMeta()) {
            return;
        }
        ItemMeta meta = itemStack.getItemMeta();
        if (!meta.hasLore()) {
            return;
        }
        List<String> lores = meta.getLore();
        String raw = lores.get(0);
        String fuel = raw.trim().split(":")[1];

        event.setCancelled(true);
        event.setUseInteractedBlock(Event.Result.DENY);
        event.setUseItemInHand(Event.Result.DENY);

        Player player = event.getPlayer();
        player.getInventory().remove(itemStack);
        Location whereToSpawn = event.getClickedBlock().getLocation().add(0.5, 1.0, 0.5);
        CarEntities.tow(player.getUniqueId());
        CarEntities.spawn(player.getUniqueId(), model, whereToSpawn, Float.valueOf(fuel));
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
