package dev.sky_lock.mocar.task;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.Car;
import dev.sky_lock.mocar.car.CarEntities;
import dev.sky_lock.mocar.util.MessageUtil;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author sky_lock
 */

public class BurnExplosionTask {

    public void run(Car car) {
        new BukkitRunnable() {
            int count = MoCar.getInstance().getPluginConfig().getWarningCount();
            BurnExplosionWarning warning = new BurnExplosionWarning();

            @Override
            public void run() {
                if (car.getPassengers().isEmpty()) {
                    if (count == 0) {
                        car.explode();
                        CarEntities.kill(car);
                        cancel();
                    }
                    count--;
                    return;
                }
                if (count == 0) {
                    car.explode();
                    CarEntities.getOwner(car).ifPresent(ownerUuid -> {
                        Player owner = Bukkit.getPlayer(ownerUuid);
                        if (owner == null) {
                            return;
                        }
                        if (car.getPassengers().stream().anyMatch(player -> player.getUniqueId().equals(ownerUuid))) {
                            return;
                        }
                        owner.sendMessage(MoCar.PREFIX + ChatColor.RED + "所有する車が" + car.getPassengers().get(0).getName() + "の運転によって破壊されました");
                    });
                    car.getPassengers().forEach(player -> {
                        warning.stop(player);
                        ((EntityPlayer) ((CraftPlayer) player).getHandle()).killEntity();
                    });
                    CarEntities.kill(car);
                    cancel();
                    return;
                }
                warning.setCount(count);
                car.getPassengers().forEach(player -> {
                    warning.send(player);
                    MessageUtil.sendDebugMessage(player.getName());
                });
                count--;
            }
        }.runTaskTimer(MoCar.getInstance(), 5L, 20L);
    }
}
