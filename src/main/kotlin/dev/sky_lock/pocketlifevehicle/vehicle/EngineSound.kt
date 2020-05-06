package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.scheduler.BukkitTask

/**
 * @author sky_lock
 */
class EngineSound(var location: Location) {
    private var task: BukkitTask? = null
    var isCancelled: Boolean = false
    var pitch: Float = 0.0f

    fun start() {
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(VehiclePlugin.instance, Runnable {
            if (this.isCancelled) {
                this.stop()
            }
            val world = location.world
            world.playSound(location, Sound.ENTITY_PIG_HURT, 0.03f, 0.7f)
            world.playSound(location, Sound.ENTITY_MINECART_RIDING, 0.03f, 0.8f)
            world.playSound(location, Sound.ENTITY_PLAYER_BURP, 0.03f, 0.8f)
            world.playSound(location, Sound.ENTITY_ENDERMAN_DEATH, 0.03f, this.pitch)
        }, 0L, 2L)
    }

    private fun stop() {
        task?.let {
            if (!it.isCancelled) {
                it.cancel()
            }
        }
    }

}