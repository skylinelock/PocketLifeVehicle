package dev.sky_lock.mocar.listener;

import dev.sky_lock.mocar.car.CarArmorStand;
import dev.sky_lock.mocar.car.CarEntity;
import dev.sky_lock.mocar.car.CraftCar;
import dev.sky_lock.mocar.gui.CarEntityUtility;
import dev.sky_lock.mocar.gui.api.GuiWindow;
import dev.sky_lock.mocar.packet.ActionBar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

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
            CarEntity entity = ((CarArmorStand) craftCar.getHandle()).getCarEntity();
            CarEntityUtility gui = new CarEntityUtility(player, entity);
            gui.open(player);
            return;
        }

        CarArmorStand car = (CarArmorStand) craftCar.getHandle();
        UUID carOwner = car.getCarEntity().getOwner();
        if (car.getCarEntity().getOwner().equals(player.getUniqueId())) {
            craftCar.setPassenger(player);
        } else {
            ActionBar.sendPacket(player, ChatColor.RED + "" + ChatColor.BOLD + "この車は " + Bukkit.getPlayer(carOwner).getName() + " が所有しています");
        }

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        GuiWindow.click(event);
    }
}
