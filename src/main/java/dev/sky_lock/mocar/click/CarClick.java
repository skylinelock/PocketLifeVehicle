package dev.sky_lock.mocar.click;

import dev.sky_lock.mocar.Permission;
import dev.sky_lock.mocar.car.CarArmorStand;
import dev.sky_lock.mocar.car.CarEntities;
import dev.sky_lock.mocar.car.CraftCar;
import dev.sky_lock.mocar.gui.CarEntityUtility;
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
        UUID owner = CarEntities.getOwner(car);
        if (owner == null) {
            return;
        }
        UUID clicked = player.getUniqueId();
        String ownerName = PlayerInfo.getName(owner);
        if (player.isSneaking()) {
            if (!clicked.equals(owner) && !Permission.CAR_CLICK.obtained(player)) {
                sendFailureInfo("この車は " + ownerName + " が所有しています");
                return;
            }
            CarEntityUtility gui = new CarEntityUtility(player, car);
            gui.open(player);
            return;
        }
        if (clicked.equals(owner)) {
            if (car.getStatus().isLocked()) {
                sendFailureInfo("車に乗るためには解錠してください");
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
    }

    private void sendFailureInfo(String message) {
        ActionBar.sendPacket(player, ChatColor.RED + "" + ChatColor.BOLD + message);
    }
}
