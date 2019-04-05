package dev.sky_lock.mocar.listener;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.*;
import dev.sky_lock.mocar.click.CarClick;
import dev.sky_lock.mocar.click.InventoryClick;
import dev.sky_lock.mocar.gui.EditSessions;
import dev.sky_lock.mocar.gui.StringEditor;
import dev.sky_lock.mocar.packet.ActionBar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * @author sky_lock
 */

public class EventListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked().getType() != EntityType.ARMOR_STAND) {
            return;
        }
        ArmorStand as = (ArmorStand) event.getRightClicked();
        if (as instanceof CarArmorStand.CraftCar) {
            event.setCancelled(true);
            return;
        }
        if (!(as instanceof SeatArmorStand.CraftSeat)) {
            return;
        }
        SeatArmorStand.CraftSeat seat = (SeatArmorStand.CraftSeat) as;
        event.setCancelled(true);
        new CarClick(event.getPlayer(), seat).accept();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        new InventoryClick(event).accept();
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        StringEditor.close(player);
        Bukkit.getScheduler().runTaskLater(MoCar.getInstance(), () -> {
            if (player.getOpenInventory().getTopInventory().getType() == InventoryType.CRAFTING) {
                EditSessions.destroy(player.getUniqueId());
            }
        }, 1L);
    }

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
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        StringEditor.close(event.getPlayer());
        EditSessions.destroy(event.getPlayer().getUniqueId());

        Player player = event.getPlayer();
        Entity vehicle = player.getVehicle();
        if (vehicle == null) {
            return;
        }
        if (!(vehicle instanceof SeatArmorStand.CraftSeat)) {
            return;
        }
        SeatArmorStand.CraftSeat seat = (SeatArmorStand.CraftSeat) vehicle;
        seat.removePassenger(player);
    }

}
