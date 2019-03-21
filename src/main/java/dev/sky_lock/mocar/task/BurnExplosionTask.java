package dev.sky_lock.mocar.task;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.CarArmorStand;
import dev.sky_lock.mocar.car.CarEntities;
import dev.sky_lock.mocar.packet.FakeExplosionPacket;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author sky_lock
 */

public class BurnExplosionTask {

    public void run(CarArmorStand car) {
        new BukkitRunnable() {
            int count = 5;
            BurnExplosionWarning warning = new BurnExplosionWarning();

            @Override
            public void run() {
                if (car.passengers == null || car.passengers.isEmpty()) {
                    if (count == 0) {
                        explode(car);
                        CarEntities.kill(car);
                        cancel();
                    }
                    count--;
                    return;
                }
                EntityLiving passenger = (EntityLiving) car.passengers.get(0);
                if (!(passenger instanceof EntityPlayer)) {
                    CarEntities.kill(car);
                    return;
                }

                Player player = ((Player) passenger.getBukkitEntity());
                if (count == 0) {
                    explode(car);
                    Player owner = Bukkit.getPlayer(CarEntities.getOwner(car));
                    if (owner != null && !player.getUniqueId().equals(owner.getUniqueId())) {
                        owner.sendMessage(MoCar.PREFIX + ChatColor.RED + "所有する車が" + player.getName() + "の運転によって破壊されました");
                    }
                    warning.stop(player);
                    passenger.killEntity();
                    CarEntities.kill(car);
                    cancel();
                    return;
                }
                warning.setCount(count);
                warning.send(player);
                count--;
            }
        }.runTaskTimer(MoCar.getInstance(), 5L, 20L);
    }

    private void explode(CarArmorStand car) {
        FakeExplosionPacket explosion = new FakeExplosionPacket();
        explosion.setX(car.locX);
        explosion.setY(car.locY);
        explosion.setZ(car.locZ);
        explosion.setRadius(5);
        explosion.broadCast();
        car.getLocation().getWorld().playSound(car.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
    }
}
