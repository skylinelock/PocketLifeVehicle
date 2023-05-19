package dev.sky_lock.pocketlifevehicle.vehicle.entity.nms

import dev.sky_lock.pocketlifevehicle.VehicleEntityType
import dev.sky_lock.pocketlifevehicle.text.ext.sendActionBar
import dev.sky_lock.pocketlifevehicle.vehicle.entity.SeatPosition
import dev.sky_lock.pocketlifevehicle.vehicle.entity.VehicleStatus
import dev.sky_lock.pocketlifevehicle.vehicle.model.SeatOption
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.decoration.ArmorStand
import net.minecraft.world.level.Level
import org.bukkit.Location
import org.bukkit.entity.Player
import kotlin.math.atan
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * @author sky_lock
 */
class SeatArmorStand : BaseArmorStand<SeatArmorStand> {
    private var status: VehicleStatus? = null
    private var position: SeatPosition? = null

    constructor(entityTypes: EntityType<ArmorStand>, world: Level) : super(entityTypes, world) {
        this.kill()
    }

    constructor(level: Level, x: Double, y: Double, z: Double) : super(
        VehicleEntityType.SEAT.type(), level, x, y, z
    )

    val passenger: Player?
        get() {
            if (!isVehicle) return null
            val passenger = bukkitEntity.passenger
            return if (passenger is Player) passenger else null
        }

    val isDriverSeat: Boolean
        get() = position === SeatPosition.ONE_DRIVER ||
            position === SeatPosition.TWO_DRIVER ||
            position === SeatPosition.FOUR_DRIVER

    fun assemble(status: VehicleStatus, position: SeatPosition) {
        this.status = status
        this.position = position
        val center = status.location
        val loc = calcSeatPosition(center, status.model.seatOption, position)
        super.absMoveTo(loc.x, center.y - 1.675 + status.model.height, loc.z, center.yaw, center.pitch)
    }

    // 毎tick呼び出される
    override fun aiStep() {
        if (!isDriverSeat) {
            synchronize()
            return
        }
        if (passengers.isEmpty()) {
            synchronize()
            return
        }
        val passenger = passengers[0] as LivingEntity
        if (passenger !is net.minecraft.world.entity.player.Player) {
            synchronize()
            return
        }
        (passenger.bukkitEntity as Player).sendActionBar(status!!.meterPanelLine())
        synchronize()
    }

    private fun synchronize() {
        val status = status!!
        val loc = calcSeatPosition(status.location, status.model.seatOption, position!!)
        xo = loc.x
        yo = status.location.y - 1.675 + status.model.height
        zo = loc.z
        setPos(xo, yo, zo)
        yRot = status.location.yaw
        yRotO = yRot
        xRot = status.location.pitch
        setRot(yRot, xRot)
    }

    private fun calcSeatPosition(location: Location, seatOption: SeatOption, seatPos: SeatPosition): Location {
        val loc = location.clone()

        val offset = seatOption.offset
        val depth = seatOption.depth
        val width = seatOption.width

        val unit = loc.direction
        val vec = unit.clone().multiply(offset)
        val origin = loc.add(vec)

        when (seatPos) {
            SeatPosition.ONE_DRIVER -> return origin
            SeatPosition.TWO_DRIVER -> return origin
            SeatPosition.TWO_PASSENGER -> {
                val vec2 = unit.clone().rotateAroundY(Math.PI).multiply(depth)
                return origin.add(vec2)
            }
            else -> {}
        }

        val distance = sqrt((depth.pow(2) + width.pow(2)).toDouble())
        val rad = atan(2 * depth / width)
        val vertical = Math.PI / 2
        val theta = if (rad.isNaN()) vertical else rad.toDouble()

        when (seatPos) {
            SeatPosition.FOUR_DRIVER -> {
                val vec2 = unit.multiply(width / 2).rotateAroundY(-vertical)
                return origin.add(vec2)
            }
            SeatPosition.FOUR_PASSENGER -> {
                val vec2 = unit.multiply(width / 2).rotateAroundY(vertical)
                return origin.add(vec2)
            }
            SeatPosition.FOUR_REAR_LEFT -> {
                val vec2 = unit.multiply(distance).rotateAroundY(vertical + theta)
                return origin.add(vec2)
            }
            SeatPosition.FOUR_REAR_RIGHT -> {
                val vec2 = unit.multiply(distance).rotateAroundY(-(vertical + theta))
                return origin.add(vec2)
            }
            else -> {
                throw IllegalStateException()
            }
        }
    }

}