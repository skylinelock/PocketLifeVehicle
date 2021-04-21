package dev.sky_lock.pocketlifevehicle

import dev.sky_lock.pocketlifevehicle.vehicle.VehicleManager
import games.pocketlife.play.VehicleAPI
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */

class VehicleAPIImpl: VehicleAPI {

    val plugin   = VehiclePlugin.instance

    override fun respawn(player: Player): Boolean {
        return VehicleManager.respawn(player)
    }

    override fun restore(player: Player): Boolean {
        return VehicleManager.restore(player)
    }

/*    override fun printLocation(player: Player): Boolean {
        return VehicleManager.printLocation(player)
    }*/
}