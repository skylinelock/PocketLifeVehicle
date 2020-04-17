package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.gui.CarUtilMenu
import dev.sky_lock.pocketlifevehicle.packet.FakeExplosionPacket
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import java.util.*
import java.util.function.Consumer

/**
 * @author sky_lock
 */
open class Car internal constructor(val model: Model) {
    val seats: MutableList<SeatArmorStand> = ArrayList()
    var center: CarArmorStand? = null
        private set
    private var menu: CarUtilMenu? = null
    val status: CarStatus = CarStatus()
    val soundTask: CarSoundTask
    val engine: Engine
    val steering: Steering
    val meterPanel: MeterPanel
    var isBeginExplode = false

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
        center = CarArmorStand((location.world as CraftWorld).handle, location.x, location.y, location.z)
        status.location = location
        center!!.assemble(this)
        status.yaw = location.yaw
        val world = center!!.world
        world.addEntity(center)
    }

    val isInWater: Boolean
        get() = center!!.isInWater

    operator fun contains(seat: SeatArmorStand): Boolean {
        return seats.contains(seat)
    }

    operator fun contains(basis: CarArmorStand): Boolean {
        return center == basis
    }

    val passengers: List<Player>
        get() = seats.filter { seat -> seat.passengers.isNotEmpty() }.map { seat: SeatArmorStand -> seat.passengers[0].bukkitEntity as CraftPlayer }

    val driver: Player?
        get() = seats.find { seat -> seat.isDriverSheet && seat.passengers.isNotEmpty() }?.passenger

    fun kill() {
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

    init {
        engine = Engine(status, model)
        steering = Steering(status)
        meterPanel = MeterPanel(status, model, engine)
        soundTask = CarSoundTask(model, status)
    }
}