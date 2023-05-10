package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.VehicleEntityType
import dev.sky_lock.pocketlifevehicle.extension.chat.Line
import dev.sky_lock.pocketlifevehicle.extension.chat.sendActionBar
import dev.sky_lock.pocketlifevehicle.extension.kotlin.truncateToOneDecimalPlace
import dev.sky_lock.pocketlifevehicle.vehicle.model.SeatOption
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeMap
import net.minecraft.world.entity.decoration.ArmorStand
import net.minecraft.world.level.Level
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import java.util.stream.IntStream
import kotlin.math.*

/**
 * @author sky_lock
 */
class SeatArmorStand : ArmorStand {
    private var status: VehicleStatus? = null
    private var position: SeatPosition? = null

    constructor(entityTypes: EntityType<out ArmorStand>, world: Level) : super(entityTypes, world) {
        this.kill()
    }

    constructor(world: Level, x: Double, y: Double, z: Double) : super(
        VehicleEntityType.SEAT.type(), world
    ) {
        super.setPos(x, y, z)
        this.readAdditionalSaveData(EntityVehicleHelper.seatNBT())

        this.craftAttributes.registerAttribute(Attribute.GENERIC_MAX_HEALTH)
        this.craftAttributes.registerAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)
        this.craftAttributes.registerAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
        this.craftAttributes.registerAttribute(Attribute.GENERIC_ARMOR)
        this.craftAttributes.registerAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS)
    }

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
        this.absMoveTo(loc.x, center.y - 1.675 + status.model.height, loc.z, center.yaw, center.pitch)
    }

    override fun getAttributes() : AttributeMap {
        return AttributeMap(LivingEntity.createLivingAttributes().build())
    }

    //足音がなるかどうか
    override fun isSilent() = true

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
        displayMeterPanel(passenger.bukkitEntity as Player)
        synchronize()
    }

    private fun synchronize() {
        val vehicle = status!!
        val loc = calcSeatPosition(vehicle.location, vehicle.model.seatOption, position!!)
        xo = loc.x
        yo = vehicle.location.y - 1.675 + vehicle.model.height
        zo = loc.z
        setPos(xo, yo, zo)
        yRot = vehicle.location.yaw
        yRotO = yRot
        xRot = vehicle.location.pitch
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
            SeatPosition.TWO_DRIVER -> {
                return origin
            }
            SeatPosition.TWO_PASSENGER -> {
                val vec2 = unit.clone().rotateAroundY(Math.PI).multiply(depth)
                return origin.add(vec2)
            }
            else -> {
            }
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

    private fun displayMeterPanel(player: Player) {
        val vehicle = status!!
        val model = vehicle.model
        val tank = vehicle.tank
        val speed = vehicle.engine.speed
        val engine = vehicle.engine

        val line = Line()
        if (!vehicle.model.flag.eventOnly) {
            line.redBold("E ")
            val fuelRate = tank.fuel / model.spec.maxFuel
            val filled = (70 * fuelRate).roundToInt()
            IntStream.range(0, filled).forEach { line.green("ǀ") }
            IntStream.range(0, 70 - filled).forEach { line.red("ǀ") }
            line.greenBold(" F   ")
            if (speed.isApproximateZero) {
                line.darkPurpleBold("P   ")
            } else {
                if (speed.isPositive) {
                    line.darkPurpleBold("D   ")
                } else if (speed.isNegative) {
                    line.darkPurpleBold("R   ")
                }
            }
        }
        val blockPerSecond = abs(engine.speedPerSecond()).truncateToOneDecimalPlace()
        line.darkGreenBold(blockPerSecond).grayBold(" blocks/s")
        player.sendActionBar(line)
    }
}