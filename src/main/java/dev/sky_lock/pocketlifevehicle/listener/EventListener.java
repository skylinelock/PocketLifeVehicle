package dev.sky_lock.pocketlifevehicle.listener;

import dev.sky_lock.pocketlifevehicle.PLVehicle;
import dev.sky_lock.pocketlifevehicle.Permission;
import dev.sky_lock.pocketlifevehicle.click.CarClick;
import dev.sky_lock.pocketlifevehicle.click.InventoryClick;
import dev.sky_lock.pocketlifevehicle.gui.EditSessions;
import dev.sky_lock.pocketlifevehicle.gui.StringEditor;
import dev.sky_lock.pocketlifevehicle.util.Formats;
import dev.sky_lock.pocketlifevehicle.vehicle.CarArmorStand;
import dev.sky_lock.pocketlifevehicle.vehicle.CarEntities;
import dev.sky_lock.pocketlifevehicle.vehicle.Storage;
import dev.sky_lock.pocketlifevehicle.vehicle.SeatArmorStand;
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
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
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
        Bukkit.getScheduler().runTaskLater(PLVehicle.getInstance(), () -> {
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
        Model model = Storage.MODEL.findByItemStack(itemStack);
        if (model == null) {
            return;
        }
        event.setCancelled(true);
        event.setUseInteractedBlock(Event.Result.DENY);
        event.setUseItemInHand(Event.Result.DENY);
        ItemMeta meta = Objects.requireNonNull(itemStack.getItemMeta());
        Player player = event.getPlayer();

        if (!PLVehicle.getInstance().getPluginConfig().getAllowWorlds().contains(event.getPlayer().getWorld())) {
            player.sendActionBar(ChatColor.RED + "このワールドでは車は使用できません");
            return;
        }
        if (event.getBlockFace() != BlockFace.UP) {
            player.sendActionBar(ChatColor.RED + "乗り物は地面にのみ設置できます");
            return;
        }
        Location whereToSpawn = Objects.requireNonNull(event.getClickedBlock()).getLocation().clone().add(0.5, 1.0, 0.5);

        if (!meta.hasLore()) {
            this.placeCarEntity(player, itemStack, event.getHand(), player.getUniqueId(), model, player.getLocation(), model.getSpec().getMaxFuel());
            return;
        }
        String ownerUUID = meta.getPersistentDataContainer().get(PLVehicle.getInstance().createKey("owner"), PersistentDataType.STRING);
        if (ownerUUID == null) {
            this.placeCarEntity(player, itemStack, event.getHand(), player.getUniqueId(), model, player.getLocation(), model.getSpec().getMaxFuel());
            return;
        }
        UUID owner = UUID.fromString(ownerUUID);
        List<String> lore = Objects.requireNonNull(meta.getLore());
        String rawFuel = lore.get(1);
        String fuel = Formats.removeBlanks(rawFuel).split(":")[1];

        if (player.getUniqueId().equals(owner)) {
            this.placeCarEntity(player, itemStack, event.getHand(), owner, model, whereToSpawn, Float.parseFloat(fuel));
            return;
        }
        if (!Permission.CAR_PLACE.obtained(player)) {
            player.sendActionBar(ChatColor.RED + "この車を所有していないので設置できません");
            return;
        }
        this.placeCarEntity(player, itemStack, event.getHand(), owner, model, whereToSpawn, Float.parseFloat(fuel));

    }

    private void placeCarEntity(Player whoPlaced, ItemStack carItem, EquipmentSlot hand, UUID owner, Model model, Location location, float fuel) {
        CarEntities.tow(owner);
        if (CarEntities.spawn(owner, model, location, fuel)) {
            location.getWorld().playSound(location, Sound.BLOCK_IRON_DOOR_OPEN, 1.0F, 1.0F);
            if (hand == EquipmentSlot.OFF_HAND) {
                whoPlaced.getInventory().setItemInOffHand(null);
            } else {
                whoPlaced.getInventory().remove(carItem);
            }
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
