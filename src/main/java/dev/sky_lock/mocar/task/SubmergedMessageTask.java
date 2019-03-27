package dev.sky_lock.mocar.task;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.CarArmorStand;
import dev.sky_lock.mocar.car.CarEntities;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author sky_lock
 */

public class SubmergedMessageTask {

    public void run(CarArmorStand carArmorStand) {
        if (carArmorStand.passengers == null || carArmorStand.passengers.isEmpty()) {
            CarEntities.kill(carArmorStand);
            return;
        }
        EntityLiving passenger = (EntityLiving) carArmorStand.passengers.get(0);
        if (!(passenger instanceof EntityPlayer)) {
            CarEntities.kill(carArmorStand);
            return;
        }
        Player player = ((Player) passenger.getBukkitEntity());
        SubmergedWarning warning = new SubmergedWarning();
        new BukkitRunnable() {
            int count = MoCar.getInstance().getPluginConfig().getWarningCount();

            @Override
            public void run() {
                if (!carArmorStand.isInWater()) {
                    warning.stop(player);
                    cancel();
                    return;
                }
                if (count != 0) {
                    warning.setCount(count);
                    warning.send(player);
                    count--;
                    return;
                }
                CarEntities.getOwner(carArmorStand).ifPresent(ownerUuid -> {
                    if (player.getUniqueId().equals(ownerUuid)) {
                        return;
                    }
                    Player ownPlayer = Bukkit.getPlayer(ownerUuid);
                    if (ownPlayer != null) {
                        ownPlayer.sendMessage(MoCar.PREFIX + ChatColor.RED + "所有する車が" + player.getName() + "の運転によって破壊されました");
                    }
                });
                CarEntities.kill(carArmorStand);
                warning.stop(player);
                cancel();
            }

        }.runTaskTimer(MoCar.getInstance(), 5L, 20L);
    }
}
