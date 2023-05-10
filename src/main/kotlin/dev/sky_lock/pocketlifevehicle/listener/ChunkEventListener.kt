package dev.sky_lock.pocketlifevehicle.listener

import dev.sky_lock.pocketlifevehicle.vehicle.EntityVehicleAdapter
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
                    val adapter = EntityVehicleAdapter.fromBukkit(entity) ?: return@forEach
                    adapter.unload()
                }
    }

    @EventHandler
    fun onChunkLoad(event: ChunkLoadEvent) {
        event.chunk.entities
            .forEach { entity ->
                val adapter = EntityVehicleAdapter.fromBukkit(entity) ?: return@forEach
                adapter.load()
            }
    }
}