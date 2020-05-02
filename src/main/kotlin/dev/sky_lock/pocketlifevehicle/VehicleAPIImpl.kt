package dev.sky_lock.pocketlifevehicle

import com.life.pocket.VehicleAPI
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */

class VehicleAPIImpl: VehicleAPI {

    override fun respawn(player: Player): Boolean {
        Bukkit.broadcastMessage("respawn")
        return true
    }

    override fun restore(player: Player): Boolean {
        Bukkit.broadcastMessage("restore")
        return true
    }
}