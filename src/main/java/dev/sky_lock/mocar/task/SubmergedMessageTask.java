package dev.sky_lock.mocar.task;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.CarArmorStand;
import dev.sky_lock.mocar.car.CarEntities;
import dev.sky_lock.mocar.util.SubmergedMessage;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.EntityPlayer;
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
        SubmergedMessage warning = new SubmergedMessage();
        new BukkitRunnable() {
            int count = 5;
            @Override
            public void run() {
                if (!carArmorStand.isInWater()) {
                    warning.stop(player);
                    cancel();
                    return;
                }
                if (count == 0) {
                    CarEntities.kill(player.getUniqueId());
                    warning.stop(player);
                    cancel();
                    return;
                }
                warning.setCount(count);
                warning.send(player);
                count--;
            }
        }.runTaskTimer(MoCar.getInstance(), 5L, 20L);
    }
}
