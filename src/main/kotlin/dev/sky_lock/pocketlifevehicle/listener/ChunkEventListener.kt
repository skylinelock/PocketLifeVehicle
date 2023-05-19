package dev.sky_lock.pocketlifevehicle.listener

import dev.sky_lock.pocketlifevehicle.vehicle.entity.EntityVehicleFacade
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
                    val vehicle = EntityVehicleFacade.fromBukkit(entity) ?: return@forEach
                    vehicle.unload()
                }
    }

    @EventHandler
    fun onChunkLoad(event: ChunkLoadEvent) {
        event.chunk.entities
            .forEach { entity ->
                val vehicle = EntityVehicleFacade.fromBukkit(entity) ?: return@forEach
                vehicle.load()
            }
    }
}