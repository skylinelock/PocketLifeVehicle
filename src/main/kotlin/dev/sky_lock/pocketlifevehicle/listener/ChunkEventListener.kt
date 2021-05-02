package dev.sky_lock.pocketlifevehicle.listener

import dev.sky_lock.pocketlifevehicle.vehicle.VehicleManager
import org.bukkit.entity.ArmorStand
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.ChunkLoadEvent
import org.bukkit.event.world.ChunkUnloadEvent

/**
 * @author sky_lock
 */
class ChunkEventListener : Listener {

    @EventHandler
    fun onChunkUnload(event: ChunkUnloadEvent) {
        event.chunk.entities
                .forEach { entity ->
                    if (entity !is ArmorStand) return@forEach
                    val vehicle = VehicleManager.findVehicle(entity) ?: return@forEach
                    if (!vehicle.isLoaded) return
                    vehicle.remove()
                    vehicle.isLoaded = false
                }
    }

    @EventHandler
    fun onChunkLoad(event: ChunkLoadEvent) {
        event.chunk.entities
            .forEach { entity ->
                if (entity !is ArmorStand) return@forEach
                val vehicle = VehicleManager.findVehicle(entity) ?: return@forEach
                if (vehicle.isLoaded) return@forEach
                vehicle.spawn()
                vehicle.isLoaded = true
            }
    }
}