package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.CustomEntityTypes
import dev.sky_lock.pocketlifevehicle.vehicle.model.SeatOption
import net.minecraft.server.v1_14_R1.*
import org.bukkit.Location
import org.bukkit.entity.Player
import kotlin.math.atan
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * @author sky_lock
 */
class SeatArmorStand : EntityArmorStand {
    private var vehicle: Vehicle? = null
    private var position: SeatPosition? = null

    constructor(entityTypes: EntityTypes<out EntityArmorStand>, world: World) : super(entityTypes, world) {
        this.killEntity()
    }

    constructor(world: World, x: Double, y: Double, z: Double) : super(EntityTypes.a(CustomEntityTypes.VEHICLE_SEAT.key).get() as EntityTypes<SeatArmorStand>, world) {
        super.setPosition(x, y, z)
        this.a(EntityVehicleHelper.seatNBT())
    }

    fun assemble(vehicle: Vehicle, position: SeatPosition) {
        this.vehicle = vehicle
        this.position = position
        val center = vehicle.location
        val loc = calculateLocation(center, vehicle.model.seatOption, position)
        setLocation(loc.x, center.y - 1.675 + vehicle.model.height, loc.z, center.yaw, center.pitch)
    }

    //足音がなるかどうか
    override fun isSilent(): Boolean {
        return isCarSheet
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
        vehicle!!.meterPanel.display(((passenger as EntityHuman).bukkitEntity as Player))
        synchronize()
    }

    val isDriverSheet: Boolean
        get() = position === SeatPosition.ONE_DRIVER || position === SeatPosition.TWO_DRIVER || position === SeatPosition.FOUR_DRIVER

    private fun synchronize() {
        val loc = calculateLocation(vehicle!!.location, vehicle!!.model.seatOption, position!!)
        locX = loc.x
        locY = vehicle!!.location.y - 1.675 + vehicle!!.model.height
        locZ = loc.z
        setPosition(locX, locY, locZ)
        yaw = vehicle!!.location.yaw
        lastYaw = yaw
        pitch = vehicle!!.location.pitch
        setYawPitch(yaw, pitch)
    }

    private val isCarSheet: Boolean
        get() = position != null

//    override fun getBukkitEntity(): CraftEntity {
//        return CraftSeat(Bukkit.getServer() as CraftServer, this)
//    }

    val passenger: Player?
        get() {
            val passenger = passengers[0] ?: return null
            return if (passenger is EntityHuman) {
                passenger.bukkitEntity as Player
            } else {
                null
            }
        }

    private fun calculateLocation(location: Location, seatOption : SeatOption, seatPos: SeatPosition): Location {
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
            else -> { throw IllegalStateException() }
        }
    }
}