package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.packet.FakeExplosionPacket
import dev.sky_lock.pocketlifevehicle.vehicle.model.Capacity
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftArmorStand
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.event.entity.CreatureSpawnEvent
import java.util.*
import net.minecraft.world.entity.decoration.ArmorStand as NMSArmorStand

/**
 * 複数のアーマースタンドとVehicleのステータスからなる車のエンティティを表します。
 *
 * @author sky_lock
 */

class EntityVehicle(owner: UUID?, private val location: Location, private val model: Model, fuel: Float) {
    private val world = location.world as CraftWorld
    private val seats = mutableListOf<SeatArmorStand>()
    private var center = ModelArmorStand(
        world.handle, location.x, location.y, location.z, location.yaw
    )
    val status = VehicleStatus(owner, location, model, fuel)
    val passengers
        get() = seats.filter { seat -> seat.isVehicle }.mapNotNull { seat -> seat.passenger }

    val driverSeat
        get() = seats.find { seat -> seat.isDriverSeat }

    val driver
        get() = driverSeat?.passenger

    private fun spawn(armorStand: NMSArmorStand) {
        world.addEntity<ArmorStand>(armorStand, CreatureSpawnEvent.SpawnReason.CUSTOM)
    }

    private fun spawnSeat(position: SeatPosition) {
        val seat = SeatArmorStand(world.handle, location.x, location.y, location.z)
        seat.assemble(status, position)
        seats.add(seat)
        spawn(seat)
    }

    fun spawn() {
        when (model.seatOption.capacity) {
            Capacity.SINGLE -> {
                spawnSeat(SeatPosition.ONE_DRIVER)
            }
            Capacity.DOUBLE -> {
                spawnSeat(SeatPosition.TWO_DRIVER)
                spawnSeat(SeatPosition.TWO_PASSENGER)
            }
            Capacity.QUAD -> {
                spawnSeat(SeatPosition.FOUR_DRIVER)
                spawnSeat(SeatPosition.FOUR_PASSENGER)
                spawnSeat(SeatPosition.FOUR_REAR_LEFT)
                spawnSeat(SeatPosition.FOUR_REAR_RIGHT)
            }
        }
        if (driverSeat == null) throw IllegalStateException("Driver seat must not be null")
        center.assemble(status, driverSeat!!)
        spawn(center)
    }

    fun remove() {
        center.kill()
        seats.forEach { seat -> seat.kill() }
        seats.clear()
    }

    fun playExplosionEffect() {
        val explosion = FakeExplosionPacket()
        explosion.setX(location.x)
        explosion.setY(location.y)
        explosion.setZ(location.z)
        explosion.setRadius(5f)
        explosion.broadCast()
        location.world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f)
    }

    fun ejectPassenger(player: Player) {
        seats.find { seat -> seat.passenger != null && seat.passenger == player } ?: return
        (player as CraftPlayer).handle.stopRiding()
    }

    fun addPassenger(player: Player) {
        if (driverSeat == null || driverSeat?.isVehicle!!) {
            val passengerSeat = seats.find { seat -> !seat.isVehicle } ?: return
            passengerSeat.bukkitEntity.addPassenger(player)
            return
        }
        driverSeat!!.bukkitEntity.addPassenger(player)
    }

    fun consistsOf(armorStand: ArmorStand): Boolean {
        val handle = (armorStand as CraftArmorStand).handle
        if (handle is SeatArmorStand) {
            return seats.contains(handle)
        } else if (handle is ModelArmorStand) {
            return center == handle
        }
        return false
    }
}