package dev.sky_lock.pocketlifevehicle.vehicle

import org.bukkit.Sound

/**
 * @author sky_lock
 */

object VehicleEffects {

    fun cancelEngineSound(status: VehicleStatus) {
        status.location.getNearbyPlayers(50.0).forEach { player ->
            player.stopSound(Sound.ENTITY_PIG_HURT)
            player.stopSound(Sound.ENTITY_MINECART_RIDING)
            player.stopSound(Sound.ENTITY_PLAYER_BURP)
            player.stopSound(Sound.ENTITY_ENDERMAN_DEATH)
        }
        status.shouldPlaySound = false
    }

    fun playEngineSound(status: VehicleStatus) {
        if (!status.shouldPlaySound) return
        val speed = status.engine.speed.approximate()
        val maxSpeed = status.model.spec.maxSpeed.value
        val location = status.location
        val world = location.world
        val pitch = speed / maxSpeed
        world.playSound(location, Sound.ENTITY_PIG_HURT, 0.03f, 0.7f)
        world.playSound(location, Sound.ENTITY_MINECART_RIDING, 0.03f, 0.8f)
        world.playSound(location, Sound.ENTITY_PLAYER_BURP, 0.03f, 0.8f)
        world.playSound(location, Sound.ENTITY_ENDERMAN_DEATH, 0.03f, pitch)
    }
}