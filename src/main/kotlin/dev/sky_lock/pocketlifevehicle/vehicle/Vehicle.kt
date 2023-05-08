package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.packet.FakeExplosionPacket
import dev.sky_lock.pocketlifevehicle.vehicle.model.Capacity
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.Bukkit
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
        spawnEntity(center)

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
        spawnEntity(seat)
    }

    private fun spawnEntity(armorStand: NMSArmorStand) {
        armorStand.level.world.addEntity<ArmorStand>(armorStand, CreatureSpawnEvent.SpawnReason.CUSTOM)
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

    fun playEngineSound() {
        if (!shouldPlaySound) return
        val world = location.world
        val pitch = engine.speed.approximate() / model.spec.maxSpeed.value
        world.playSound(location, Sound.ENTITY_PIG_HURT, 0.03f, 0.7f)
        world.playSound(location, Sound.ENTITY_MINECART_RIDING, 0.03f, 0.8f)
        world.playSound(location, Sound.ENTITY_PLAYER_BURP, 0.03f, 0.8f)
        world.playSound(location, Sound.ENTITY_ENDERMAN_DEATH, 0.03f, pitch)
    }

    fun cancelEngineSound() {
        location.getNearbyPlayers(50.0).forEach { player ->
            player.stopSound(Sound.ENTITY_PIG_HURT)
            player.stopSound(Sound.ENTITY_MINECART_RIDING)
            player.stopSound(Sound.ENTITY_PLAYER_BURP)
            player.stopSound(Sound.ENTITY_ENDERMAN_DEATH)
        }
        this.shouldPlaySound = false
    }

    fun updateLocation(location: Location) {
        this.location = location
    }
}