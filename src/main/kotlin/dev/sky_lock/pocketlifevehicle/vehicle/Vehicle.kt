package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.packet.FakeExplosionPacket
import dev.sky_lock.pocketlifevehicle.vehicle.model.Capacity
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftArmorStand
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import java.util.*

/**
 * @author sky_lock
 */
class Vehicle(val owner: UUID?, var location: Location, val model: Model, fuel: Float) {
    private val seats = mutableListOf<SeatArmorStand>()
    private var center = ModelArmorStand(
        (location.world as CraftWorld).handle, location.x, location.y, location.z, location.yaw
    )

    val ownerName: String
        get() {
            val uuid = owner ?: return "unknown"
            return Bukkit.getOfflinePlayer(uuid).name ?: "unknown"
        }

    val sound = EngineSound(location)
    val tank = FuelTank(fuel, model.spec.maxFuel)
    val engine = Engine(tank, model)
    val steering = Steering(this)

    var yaw = 0f

    private var isEventOnly = model.flag.eventOnly
    var isLocked = !isEventOnly
    var shouldPlaySound = model.flag.engineSound
    var shouldAnimate = model.flag.animation
    var isLoaded = true

    var isBeginExplode = false
    var isUndrivable = false
    val isInWater
        get() = center.isInWater

    val passengers
        get() = seats.filter { seat -> seat.isVehicle }.mapNotNull { seat -> seat.passenger }

    private val driverSeat
        get() = seats.find { seat -> seat.isDriverSheet }

    val driver
        get() = driverSeat?.passenger

    init {
        location = center.location
        yaw = location.yaw
    }

    fun spawn() {
        center.assemble(this)
        val world = center.world
        world.addEntity(center)

        when (model.seatOption.capacity) {
            Capacity.SINGLE -> {
                spawnSeatArmorStand(SeatPosition.ONE_DRIVER)
            }
            Capacity.DOUBLE -> {
                spawnSeatArmorStand(SeatPosition.TWO_DRIVER)
                spawnSeatArmorStand(SeatPosition.TWO_PASSENGER)
            }
            Capacity.QUAD -> {
                spawnSeatArmorStand(SeatPosition.FOUR_DRIVER)
                spawnSeatArmorStand(SeatPosition.FOUR_PASSENGER)
                spawnSeatArmorStand(SeatPosition.FOUR_REAR_LEFT)
                spawnSeatArmorStand(SeatPosition.FOUR_REAR_RIGHT)
            }
        }
    }

    private fun spawnSeatArmorStand(position: SeatPosition) {
        val world = (location.world as CraftWorld).handle
        val seat = SeatArmorStand(world, location.x, location.y, location.z)
        seat.assemble(this, position)
        seats.add(seat)
        world.addEntity(seat)
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

    fun ejectPassenger(player: Player) {
        seats.find { seat -> seat.passenger != null && seat.passenger == player } ?: return
        (player as CraftPlayer).handle.stopRiding()
    }

    fun addPassenger(player: Player) {
        val driverSeat = driverSeat
        if (driverSeat == null || driverSeat.isVehicle) {
            val passengerSeat = seats.find { seat -> !seat.isVehicle } ?: return
            passengerSeat.bukkitEntity.addPassenger(player)
            return
        }
        driverSeat.bukkitEntity.addPassenger(player)
    }

    fun remove() {
        center.killEntity()
        seats.forEach { seat -> seat.killEntity() }
        seats.clear()
    }

    fun explode() {
        val explosion = FakeExplosionPacket()
        explosion.setX(location.x)
        explosion.setY(location.y)
        explosion.setZ(location.z)
        explosion.setRadius(5f)
        explosion.broadCast()
        location.world?.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f)
    }
}