package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.gui.CarUtilMenu
import dev.sky_lock.pocketlifevehicle.packet.FakeExplosionPacket
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftArmorStand
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import java.util.*
import java.util.function.Consumer

/**
 * @author sky_lock
 */
open class Vehicle internal constructor(val model: Model) {
    val seats: MutableList<SeatArmorStand> = ArrayList()
    var center: ModelArmorStand? = null
        private set
    private var menu: CarUtilMenu? = null
    val status: CarStatus = CarStatus()
    val engineSound: EngineSound
    val engine: Engine
    val steering: Steering
    val meterPanel: MeterPanel
    var isBeginExplode = false

    init {
        engine = Engine(status, model)
        steering = Steering(status)
        meterPanel = MeterPanel(status, model, engine)
        engineSound = EngineSound(model, status)
    }

    fun addSeat(seat: SeatArmorStand) {
        seats.add(seat)
    }

    val location: Location
        get() = center!!.location

    fun refuel(fuel: Float): Boolean {
        return engine.refuel(fuel)
    }

    fun openMenu(player: Player) {
        if (menu == null) {
            menu = CarUtilMenu(player, this)
        }
        menu!!.open(player, 0)
    }

    fun closeMenu(player: Player) {
        if (menu == null) {
            return
        }
        menu!!.close(player)
    }

    open fun spawn(location: Location) {
        center = ModelArmorStand((location.world as CraftWorld).handle, location.x, location.y, location.z)
        status.location = location
        center!!.assemble(this)
        status.yaw = location.yaw
        val world = center!!.world
        world.addEntity(center)
    }

    val isInWater: Boolean
        get() = center!!.isInWater

    fun consistsOf(armorStand: ArmorStand): Boolean {
        val handle = (armorStand as CraftArmorStand).handle
        if (handle is SeatArmorStand) {
            return seats.contains(handle)
        } else if (handle is ModelArmorStand){
            return center == handle
        }
        return false
    }

    operator fun contains(seat: SeatArmorStand): Boolean {
        return seats.contains(seat)
    }

    operator fun contains(basis: ModelArmorStand): Boolean {
        return center == basis
    }

    val passengers: List<Player>
        get() = seats.filter { seat -> seat.passengers.isNotEmpty() }.map { seat -> seat.passengers[0].bukkitEntity as CraftPlayer }

    fun addPassenger(player: Player) {
        val driverSeat = seats.find { seat -> seat.isDriverSheet && seat.passengers.isEmpty() }
        if (driverSeat == null) {
            val passengerSeat = seats.find { seat -> seat.passengers.isEmpty() } ?: return
            passengerSeat.bukkitEntity.addPassenger(player)
        } else {
            driverSeat.bukkitEntity.addPassenger(player)
        }
    }

    val driver: Player?
        get() = seats.find { seat -> seat.isDriverSheet && seat.passengers.isNotEmpty() }?.passenger

    fun scrap() {
        center!!.killEntity()
        seats.forEach(Consumer { obj: SeatArmorStand -> obj.killEntity() })
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