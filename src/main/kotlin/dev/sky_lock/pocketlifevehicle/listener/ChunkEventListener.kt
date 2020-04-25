package dev.sky_lock.pocketlifevehicle.listener

import dev.sky_lock.pocketlifevehicle.vehicle.Vehicle
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleEntities
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleEntities.getVehicle
import org.bukkit.Chunk
import org.bukkit.entity.ArmorStand
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.ChunkLoadEvent
import org.bukkit.event.world.ChunkUnloadEvent
import java.util.*

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
                    val car = getVehicle(entity) ?: return@forEach
                    car.remove()
                    vehicles.add(car)
                }
    }

    @EventHandler
    fun onChunkLoad(event: ChunkLoadEvent) {
        vehicles.removeAll { car ->
            if (!isSameChunk(car.location.chunk, event.chunk)) {
                return@removeAll false
            }
            return@removeAll VehicleEntities.spawn(car)
        }
    }

    private fun isSameChunk(chunk1: Chunk, chunk2: Chunk): Boolean {
        return chunk1.x == chunk2.x && chunk1.z == chunk2.z
    }
}