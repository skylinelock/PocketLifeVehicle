package dev.sky_lock.pocketlifevehicle.task;

import dev.sky_lock.pocketlifevehicle.PLVehicle;
import dev.sky_lock.pocketlifevehicle.vehicle.Car;
import dev.sky_lock.pocketlifevehicle.vehicle.CarEntities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author sky_lock
 */

public class SubmergedMessageTask {

    public void run(Car car) {
        SubmergedWarning warning = new SubmergedWarning();
        new BukkitRunnable() {
            int count = PLVehicle.getInstance().getPluginConfig().getWarningCount();

            @Override
            public void run() {
                if (car.getPassengers().isEmpty()) {
                    if (count == 0) {
                        CarEntities.kill(car);
                        cancel();
                        return;
                    }
                    count--;
                    return;
                }
                if (!car.isInWater()) {
                    car.getPassengers().forEach(warning::stop);
                    cancel();
                    return;
                }
                if (count != 0) {
                    warning.setCount(count);
                    car.getPassengers().forEach(warning::send);
                    count--;
                    return;
                }
                CarEntities.getOwner(car).ifPresent(ownerUuid -> {
                    if (car.getPassengers().stream().anyMatch(player -> player.getUniqueId().equals(ownerUuid))) {
                        return;
                    }
                    Player ownPlayer = Bukkit.getPlayer(ownerUuid);
                    if (ownPlayer != null) {
                        ownPlayer.sendMessage(PLVehicle.PREFIX + ChatColor.RED + "所有する車が" + car.getPassengers().get(0).getName() + "の運転によって破壊されました");
                    }
                });
                CarEntities.kill(car);
                car.getPassengers().forEach(warning::stop);
                cancel();
            }

        }.runTaskTimer(PLVehicle.getInstance(), 5L, 20L);
    }
}
