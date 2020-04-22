package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.PLVehicle
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.scheduler.BukkitTask

/**
 * @author sky_lock
 */
class EngineSound(private val model: Model, private val status: CarStatus) {
    private var task: BukkitTask? = null
    fun start() {
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(PLVehicle.instance, Runnable {
            val location = status.location
            if (location == null) {
                this.stop()
                return@Runnable
            }
            val world = location.world
            world.playSound(location, Sound.ENTITY_PIG_HURT, 0.05f, 0.7f)
            world.playSound(location, Sound.ENTITY_MINECART_RIDING, 0.05f, 0.8f)
            world.playSound(location, Sound.ENTITY_PLAYER_BURP, 0.05f, 0.8f)
            val enginePitch = status.speed.approximate() / model.spec.maxSpeed.max
            world.playSound(location, Sound.ENTITY_ENDERMAN_DEATH, 0.05f, enginePitch)
        }, 0L, 2L)
    }

    fun stop() {
        if (task == null) {
            return
        }
        if (!task!!.isCancelled) {
            task!!.cancel()
        }
    }

}