package dev.sky_lock.mocar.click;

import dev.sky_lock.mocar.Permission;
import dev.sky_lock.mocar.car.CarArmorStand;
import dev.sky_lock.mocar.car.CarEntities;
import dev.sky_lock.mocar.car.CraftCar;
import dev.sky_lock.mocar.packet.ActionBar;
import dev.sky_lock.mocar.util.PlayerInfo;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author sky_lock
 */

public class CarClick {
    private final Player player;
    private final CraftCar craftCar;

    public CarClick(Player player, CraftCar craftCar) {
        this.player = player;
        this.craftCar = craftCar;
    }

    public void accept() {
        CarArmorStand car = (CarArmorStand) craftCar.getHandle();
        CarEntities.getOwner(car).ifPresent(owner -> {
            UUID clicked = player.getUniqueId();
            String ownerName = PlayerInfo.getName(owner);
            if (player.isSneaking()) {
                if (!clicked.equals(owner) && !Permission.CAR_CLICK.obtained(player)) {
                    sendFailureInfo("この車は " + ownerName + " が所有しています");
                    return;
                }
                car.openUtilMenu(player);
                return;
            }
            if (clicked.equals(owner)) {
                if (car.getStatus().isLocked()) {
                    sendFailureInfo("乗車するためには解錠する必要があります");
                    return;
                }
                craftCar.setPassenger(player);
                return;
            }
            if (car.getStatus().isLocked()) {
                sendFailureInfo("この車は " + ownerName + " によってロックされています");
                return;
            }
            craftCar.setPassenger(player);
        });
    }

    private void sendFailureInfo(String message) {
        ActionBar.sendPacket(player, ChatColor.YELLOW + "" + ChatColor.BOLD + "⚠⚠ " + ChatColor.RED + "" + ChatColor.BOLD + message + ChatColor.YELLOW + "" + ChatColor.BOLD + " ⚠⚠");
    }
}
