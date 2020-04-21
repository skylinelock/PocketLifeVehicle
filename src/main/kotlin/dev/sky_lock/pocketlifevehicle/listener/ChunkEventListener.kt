package dev.sky_lock.pocketlifevehicle.listener

import dev.sky_lock.pocketlifevehicle.vehicle.Car
import dev.sky_lock.pocketlifevehicle.vehicle.CarArmorStand
import dev.sky_lock.pocketlifevehicle.vehicle.CarEntities
import dev.sky_lock.pocketlifevehicle.vehicle.CarEntities.getCar
import org.bukkit.Chunk
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.ChunkLoadEvent
import org.bukkit.event.world.ChunkUnloadEvent
import java.util.*

/**
 * @author sky_lock
 */
class ChunkEventListener : Listener {
    private val cars: MutableList<Car> = ArrayList()

    @EventHandler
    fun onChunkUnload(event: ChunkUnloadEvent) {
        event.chunk.entities
                .filter { entity -> (entity as CraftEntity).handle is CarArmorStand }
                .forEach { entity ->
                    val car = getCar((entity as CraftEntity).handle as CarArmorStand)
                    car!!.kill()
                    cars.add(car)
                }
    }

    @EventHandler
    fun onChunkLoad(event: ChunkLoadEvent) {
        cars.removeAll { car ->
            if (!isSameChunk(car.location.chunk, event.chunk)) {
                return@removeAll false
            }
            return@removeAll CarEntities.spawn(car)
        }
    }

    private fun isSameChunk(chunk1: Chunk, chunk2: Chunk): Boolean {
        return chunk1.x == chunk2.x && chunk1.z == chunk2.z
    }
}