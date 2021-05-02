package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.CustomEntityTypes
import dev.sky_lock.pocketlifevehicle.extension.kotlin.truncateToOneDecimalPlace
import dev.sky_lock.pocketlifevehicle.vehicle.model.SeatOption
import net.minecraft.server.v1_14_R1.*
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.stream.IntStream
import kotlin.math.*

/**
 * @author sky_lock
 */
class SeatArmorStand : EntityArmorStand {
    private var vehicle: Vehicle? = null
    private var position: SeatPosition? = null

    constructor(entityTypes: EntityTypes<out EntityArmorStand>, world: World) : super(entityTypes, world) {
        this.killEntity()
    }

    constructor(world: World, x: Double, y: Double, z: Double) : super(
        EntityTypes.a(CustomEntityTypes.VEHICLE_SEAT.key).get() as EntityTypes<SeatArmorStand>, world
    ) {
        super.setPosition(x, y, z)
        this.a(EntityVehicleHelper.seatNBT())
    }

    val passenger: Player?
        get() {
            if (!isVehicle) return null
            val passenger = bukkitEntity.passenger
            return if (passenger is Player) passenger else null
        }

    val isDriverSheet: Boolean
        get() = position === SeatPosition.ONE_DRIVER ||
            position === SeatPosition.TWO_DRIVER ||
            position === SeatPosition.FOUR_DRIVER

    fun assemble(vehicle: Vehicle, position: SeatPosition) {
        this.vehicle = vehicle
        this.position = position
        val center = vehicle.location
        val loc = calcSeatPosition(center, vehicle.model.seatOption, position)
        setLocation(loc.x, center.y - 1.675 + vehicle.model.height, loc.z, center.yaw, center.pitch)
    }

    //足音がなるかどうか
    override fun isSilent(): Boolean {
        return true
    }

    override fun movementTick() {
        if (!isDriverSheet) {
            synchronize()
            return
        }
        if (vehicle!!.driver == null) {
            synchronize()
            return
        }
        val passenger = passengers[0] as EntityLiving
        if (passenger !is EntityPlayer) {
            synchronize()
            return
        }
        displayMeterPanel(((passenger as EntityHuman).bukkitEntity as Player))
        synchronize()
    }

    private fun synchronize() {
        val vehicle = vehicle!!
        val loc = calcSeatPosition(vehicle.location, vehicle.model.seatOption, position!!)
        locX = loc.x
        locY = vehicle.location.y - 1.675 + vehicle.model.height
        locZ = loc.z
        setPosition(locX, locY, locZ)
        yaw = vehicle.location.yaw
        lastYaw = yaw
        pitch = vehicle.location.pitch
        setYawPitch(yaw, pitch)
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
        val vehicle = vehicle!!
        val model = vehicle.model
        val tank = vehicle.tank
        val speed = vehicle.engine.speed
        val engine = vehicle.engine

        val builder = StringBuilder()
        if (!vehicle.model.flag.eventOnly) {
            builder.append(ChatColor.RED).append(ChatColor.BOLD).append("E ").append(ChatColor.GREEN)
            val fuelRate = tank.fuel / model.spec.maxFuel
            val filled = (70 * fuelRate).roundToInt()
            IntStream.range(0, filled).forEach { builder.append("ǀ") }
            builder.append(ChatColor.RED)
            IntStream.range(0, 70 - filled).forEach { builder.append("ǀ") }
            builder.append(" ").append(ChatColor.GREEN).append(ChatColor.BOLD).append(" F").append("   ").append(
                ChatColor.DARK_PURPLE
            ).append(ChatColor.BOLD)
            if (speed.isApproximateZero) {
                builder.append("P")
            } else {
                if (speed.isPositive) {
                    builder.append("D")
                } else if (speed.isNegative) {
                    builder.append("R")
                }
            }
            builder.append("   ")
        }
        builder.append(ChatColor.DARK_GREEN).append(ChatColor.BOLD)
        val blockPerSecond = abs(engine.speedPerSecond()).truncateToOneDecimalPlace()
        builder.append(blockPerSecond).append(ChatColor.GRAY).append(ChatColor.BOLD).append(" blocks/s")
        player.sendActionBar(builder.toString())
    }
}