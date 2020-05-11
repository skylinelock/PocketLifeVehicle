package dev.sky_lock.pocketlifevehicle

import com.life.pocket.VehicleAPI
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleEntities
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */

class VehicleAPIImpl: VehicleAPI {

    val plugin = VehiclePlugin.instance

    override fun respawn(player: Player): Boolean {
        return VehicleEntities.respawn(player)
    }

    override fun restore(player: Player): Boolean {
        return VehicleEntities.restore(player)
    }
}