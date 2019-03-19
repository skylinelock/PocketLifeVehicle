package dev.sky_lock.mocar.listener;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.CarArmorStand;
import dev.sky_lock.mocar.car.CarEntities;
import dev.sky_lock.mocar.car.CraftCar;
import dev.sky_lock.mocar.gui.*;
import dev.sky_lock.mocar.gui.api.GuiWindow;
import dev.sky_lock.mocar.packet.ActionBar;
import net.minecraft.server.v1_12_R1.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

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
        Player player = event.getPlayer();
        CraftCar craftCar = (CraftCar) as;
        if (player.isSneaking()) {
            CarArmorStand carArmorStand = (CarArmorStand) craftCar.getHandle();
            UUID owner = CarEntities.getOwner(carArmorStand);
            if (!event.getPlayer().getUniqueId().equals(owner)) {
                return;
            }
            CarEntityUtility gui = new CarEntityUtility(player);
            gui.open(player);
            return;
        }

        CarArmorStand car = (CarArmorStand) craftCar.getHandle();
        UUID carOwner = CarEntities.getOwner(car);
        if (carOwner.equals(player.getUniqueId())) {
            craftCar.setPassenger(player);
        } else {
            ActionBar.sendPacket(player, ChatColor.RED + "" + ChatColor.BOLD + "この車は " + Bukkit.getPlayer(carOwner).getName() + " が所有しています");
        }

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        GuiWindow.click(event);
        ItemStack itemStack = CraftItemStack.asNMSCopy(event.getCurrentItem());
        if (!itemStack.hasTag()) {
            return;
        }
        if (!itemStack.getTag().hasKey("editor-result")) {
            return;
        }
        event.setResult(Event.Result.DENY);
        event.setCancelled(true);
        org.bukkit.inventory.ItemStack result = event.getCurrentItem();

        StringEditor editor = StringEditor.get((Player) event.getWhoClicked());
        EditModelData editData = EditSessions.get(event.getWhoClicked().getUniqueId());

        if (editor.getEditorType() == StringEditor.Type.ID) {
            editData.setId(result.getItemMeta().getDisplayName());
        } else if (editor.getEditorType() == StringEditor.Type.NAME) {
            editData.setName(result.getItemMeta().getDisplayName());
        }
        Player player = (Player) event.getWhoClicked();
        StringEditor.close(player);
        new ModelSetting(player).open(player);
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

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        GuiWindow.drag(event);
    }

}
