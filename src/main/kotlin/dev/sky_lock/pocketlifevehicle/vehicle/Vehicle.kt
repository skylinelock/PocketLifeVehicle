package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.gui.CarUtilMenu
import dev.sky_lock.pocketlifevehicle.packet.FakeExplosionPacket
import dev.sky_lock.pocketlifevehicle.vehicle.model.Capacity
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
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
open class Vehicle constructor(val model: Model) {
    private val seats: MutableList<SeatArmorStand> = ArrayList()
    private var center: ModelArmorStand? = null

    private var menu: CarUtilMenu? = null
    val state: State = State()
    lateinit var engineSound: EngineSound
    val engine: Engine
    val steering: Steering
    val meterPanel: MeterPanel
    var isBeginExplode = false
    var isUndrivable = false

    init {
        engine = Engine(state, model)
        steering = Steering(state)
        meterPanel = MeterPanel(state, model, engine)
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
        center = ModelArmorStand((location.world as CraftWorld).handle, location.x, location.y, location.z, location.yaw)
        state.location = location
        // EngineSound初期化してからassemble
        this.engineSound = EngineSound(location)
        center!!.assemble(this)
        state.yaw = location.yaw
        val world = center!!.world
        world.addEntity(center)

        when (model.capacity) {
            Capacity.SINGLE -> {
                val driver = SeatArmorStand(center!!.getWorld(), location.x, location.y, location.z)
                driver.assemble(this, SeatPosition.ONE_DRIVER)
                seats.add(driver)
                seats.forEach { entity: SeatArmorStand -> center!!.getWorld().addEntity(entity) }
            }
            Capacity.DOUBLE -> {
                val driver = SeatArmorStand(center!!.getWorld(), location.x, location.y, location.z)
                driver.assemble(this, SeatPosition.TWO_DRIVER)
                val passenger = SeatArmorStand(center!!.getWorld(), location.x, location.y, location.z)
                passenger.assemble(this, SeatPosition.TWO_PASSENGER)
                seats.add(driver)
                seats.add(passenger)
                seats.forEach { entity: SeatArmorStand -> center!!.getWorld().addEntity(entity) }
            }
            Capacity.QUAD -> {
                val driver = SeatArmorStand(center!!.getWorld(), location.x, location.y, location.z)
                driver.assemble(this, SeatPosition.FOUR_DRIVER)
                val passenger = SeatArmorStand(center!!.getWorld(), location.x, location.y, location.z)
                passenger.assemble(this, SeatPosition.FOUR_PASSENGER)
                val rearRight = SeatArmorStand(center!!.getWorld(), location.x, location.y, location.z)
                rearRight.assemble(this, SeatPosition.FOUR_REAR_RIGHT)
                val rearLeft = SeatArmorStand(center!!.getWorld(), location.x, location.y, location.z)
                rearLeft.assemble(this, SeatPosition.FOUR_REAR_LEFT)
                seats.add(driver)
                seats.add(passenger)
                seats.add(rearRight)
                seats.add(rearLeft)
                seats.forEach { entity: SeatArmorStand? -> center!!.getWorld().addEntity(entity) }
            }
        }
    }

    val isInWater: Boolean
        get() = center!!.isInWater

    fun consistsOf(armorStand: ArmorStand): Boolean {
        val handle = (armorStand as CraftArmorStand).handle
        if (handle is SeatArmorStand) {
            return seats.contains(handle)
        } else if (handle is ModelArmorStand) {
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

    fun remove() {
        center!!.killEntity()
        seats.forEach { obj: SeatArmorStand -> obj.killEntity() }
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