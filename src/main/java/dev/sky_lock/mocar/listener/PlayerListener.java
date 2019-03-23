package dev.sky_lock.mocar.listener;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.CarEntities;
import dev.sky_lock.mocar.car.CarModel;
import dev.sky_lock.mocar.car.CraftCar;
import dev.sky_lock.mocar.car.ModelList;
import dev.sky_lock.mocar.packet.ActionBar;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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
        event.setCancelled(true);
        event.setUseInteractedBlock(Event.Result.DENY);
        event.setUseItemInHand(Event.Result.DENY);
        if (!MoCar.getInstance().getPluginConfig().getAllowWorlds().contains(event.getPlayer().getWorld())) {
            ActionBar.sendPacket(event.getPlayer(), ChatColor.RED + "このワールドでは車は使用できません");
            return;
        }
        List<String> lores = meta.getLore();
        String rawOwner = lores.get(0);
        String ownerName = rawOwner.replaceAll("\\s", "").split(":")[1];
        String rawFuel = lores.get(1);
        String fuel = rawFuel.replaceAll("\\s", "").split(":")[1];

        Player player = event.getPlayer();

        if (!player.getName().equals(ownerName)) {
            ActionBar.sendPacket(player, ChatColor.RED + "この車を所有していないので設置できません");
            return;
        }
        Location whereToSpawn = event.getClickedBlock().getLocation().add(0.5, 1.0, 0.5);
        CarEntities.tow(player.getUniqueId());
        if (CarEntities.spawn(player.getUniqueId(), model, whereToSpawn, Float.valueOf(fuel))) {
            player.getInventory().remove(itemStack);
        };
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
