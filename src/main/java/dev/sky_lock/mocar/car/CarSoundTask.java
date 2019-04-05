package dev.sky_lock.mocar.car;


import dev.sky_lock.mocar.MoCar;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author sky_lock
 */

class CarSoundTask {
    private CarStatus status;
    private BukkitTask task;
    private int soundTick;

    CarSoundTask(CarStatus status) {
        this.status = status;
    }

    void start() {
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(MoCar.getInstance(), () -> {
            Location location = status.getLocation();
            World world = location.getWorld();
            world.playSound(location, Sound.ENTITY_PIG_HURT, 0.3F, 0.7F);
            world.playSound(location, Sound.ENTITY_MINECART_RIDING, 0.2F, 0.8F);
            world.playSound(location, Sound.ENTITY_PLAYER_BURP, 0.1F, 0.8F);
        }, 0L, 2L);
    }

    void stop() {
        if (!task.isCancelled()) {
            task.cancel();
        }
    }




}
