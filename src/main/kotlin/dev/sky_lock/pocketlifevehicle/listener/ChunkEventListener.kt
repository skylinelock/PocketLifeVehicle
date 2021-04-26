package dev.sky_lock.pocketlifevehicle.listener

import dev.sky_lock.pocketlifevehicle.vehicle.Vehicle
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleManager
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleManager.getVehicle
import org.bukkit.Chunk
import org.bukkit.entity.ArmorStand
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.ChunkLoadEvent
import org.bukkit.event.world.ChunkUnloadEvent

/**
 * @author sky_lock
 */
class ChunkEventListener : Listener {
    private val vehicles: MutableList<Vehicle> = ArrayList()

    @EventHandler
    fun onChunkUnload(event: ChunkUnloadEvent) {
        event.chunk.entities
                .forEach { entity ->
                    if (entity !is ArmorStand) {
                        return@forEach
                    }
                    val vehicle = getVehicle(entity) ?: return@forEach
                    vehicle.remove()
                    vehicles.add(vehicle)
                }
    }

    @EventHandler
    fun onChunkLoad(event: ChunkLoadEvent) {
        vehicles.removeAll { vehicle ->
            if (!isSameChunk(vehicle.location.chunk, event.chunk)) {
                return@removeAll false
            }
            return@removeAll VehicleManager.placeEntity(vehicle)
        }
    }

    private fun isSameChunk(chunk1: Chunk, chunk2: Chunk): Boolean {
        return chunk1.x == chunk2.x && chunk1.z == chunk2.z
    }
}