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

    var isBeginExplode = false
    var isUndrivable = false

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

    fun spawn(location: Location) {
        center.assemble(this)
        val world = center.world
        world.addEntity(center)

        when (model.seatOption.capacity) {
            Capacity.SINGLE -> {
                val driver = SeatArmorStand(center.getWorld(), location.x, location.y, location.z)
                driver.assemble(this, SeatPosition.ONE_DRIVER)
                seats.add(driver)
                seats.forEach { entity -> center.getWorld().addEntity(entity) }
            }
            Capacity.DOUBLE -> {
                val driver = SeatArmorStand(center.getWorld(), location.x, location.y, location.z)
                driver.assemble(this, SeatPosition.TWO_DRIVER)
                val passenger = SeatArmorStand(center.getWorld(), location.x, location.y, location.z)
                passenger.assemble(this, SeatPosition.TWO_PASSENGER)
                seats.add(driver)
                seats.add(passenger)
                seats.forEach { entity -> center.getWorld().addEntity(entity) }
            }
            Capacity.QUAD -> {
                val driver = SeatArmorStand(center.getWorld(), location.x, location.y, location.z)
                driver.assemble(this, SeatPosition.FOUR_DRIVER)
                val passenger = SeatArmorStand(center.getWorld(), location.x, location.y, location.z)
                passenger.assemble(this, SeatPosition.FOUR_PASSENGER)
                val rearRight = SeatArmorStand(center.getWorld(), location.x, location.y, location.z)
                rearRight.assemble(this, SeatPosition.FOUR_REAR_RIGHT)
                val rearLeft = SeatArmorStand(center.getWorld(), location.x, location.y, location.z)
                rearLeft.assemble(this, SeatPosition.FOUR_REAR_LEFT)
                seats.add(driver)
                seats.add(passenger)
                seats.add(rearRight)
                seats.add(rearLeft)
                seats.forEach { entity -> center.getWorld().addEntity(entity) }
            }
        }
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