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
    private final CarModel model;
    private final CarStatus status;
    private BukkitTask task;
    private int soundTick;

    CarSoundTask(CarModel model, CarStatus status) {
        this.model = model;
        this.status = status;
    }

    void start() {
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(MoCar.getInstance(), () -> {
            Location location = status.getLocation();
            World world = location.getWorld();
            world.playSound(location, Sound.ENTITY_PIG_HURT, 0.009F, 0.7F);
            world.playSound(location, Sound.ENTITY_MINECART_RIDING, 0.009F, 0.8F);
            world.playSound(location, Sound.ENTITY_PLAYER_BURP, 0.009F, 0.8F);
            float engine_pitch = status.getSpeed().approximate() / model.getMaxSpeed().getMax();
            world.playSound(location, Sound.ENTITY_ENDERMAN_DEATH, 0.009F, engine_pitch);
        }, 0L, 2L);
    }

    void stop() {
        if (!task.isCancelled()) {
            task.cancel();
        }
    }




}
