package dev.sky_lock.mocar.click;

import dev.sky_lock.mocar.Permission;
import dev.sky_lock.mocar.car.Car;
import dev.sky_lock.mocar.car.CarEntities;
import dev.sky_lock.mocar.car.SeatArmorStand;
import dev.sky_lock.mocar.packet.ActionBar;
import dev.sky_lock.mocar.util.Profiles;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author sky_lock
 */

public class CarClick {
    private final Player player;
    private final SeatArmorStand.CraftSeat seat;
    private final Car car;

    public CarClick(Player player, SeatArmorStand.CraftSeat seat) {
        this.player = player;
        this.seat = seat;
        this.car = CarEntities.getCar(seat.getHandle());
    }

    public void accept() {
        if (!seat.getPassengers().isEmpty()) {
            return;
        }
        CarEntities.getOwner(car).ifPresent(owner -> {
            UUID clicked = player.getUniqueId();
            String ownerName = Profiles.getName(owner);
            if (player.isSneaking()) {
                if (!clicked.equals(owner) && !Permission.CAR_CLICK.obtained(player)) {
                    sendFailureInfo("この乗り物は " + ownerName + " が所有しています");
                    return;
                }
                car.openMenu(player);
                return;
            }
            if (clicked.equals(owner)) {
                this.seat.setPassenger(player);
                return;
            }
            if (car.getStatus().isLocked()) {
                sendFailureInfo("この乗り物には鍵が掛かっています");
                return;
            }
            this.seat.setPassenger(player);
        });
    }

    private void sendFailureInfo(String message) {
        ActionBar.sendPacket(player, ChatColor.YELLOW + "" + ChatColor.BOLD + "⚠⚠ " + ChatColor.RED + "" + ChatColor.BOLD + message + ChatColor.YELLOW + "" + ChatColor.BOLD + " ⚠⚠");
    }
}
