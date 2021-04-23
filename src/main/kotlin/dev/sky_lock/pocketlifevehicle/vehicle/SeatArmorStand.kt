package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.CustomEntityTypes
import net.minecraft.server.v1_14_R1.*
import org.bukkit.Location
import org.bukkit.entity.Player

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
        val loc = calculateLocation(center, position)
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
        val loc = calculateLocation(vehicle!!.location, position!!)
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

    private fun calculateLocation(location: Location, position: SeatPosition): Location {
        val vector = location.direction
        when (position) {
            SeatPosition.ONE_DRIVER -> return location
            SeatPosition.TWO_DRIVER -> {
                vector.multiply(1)
                return location.clone().add(vector)
            }
            SeatPosition.TWO_PASSENGER -> {
                vector.multiply(1).rotateAroundY(Math.toRadians(180.0))
                return location.clone().add(vector)
            }
            SeatPosition.FOUR_DRIVER -> {
                vector.multiply(0.5).rotateAroundY(Math.toRadians(-90.0))
                return location.clone().add(vector)
            }
            SeatPosition.FOUR_PASSENGER -> {
                vector.multiply(0.5).rotateAroundY(Math.toRadians(90.0))
                return location.clone().add(vector)
            }
            SeatPosition.FOUR_REAR_LEFT -> {
                vector.multiply(1.3).rotateAroundY(Math.toRadians(158.0))
                return location.clone().add(vector)
            }
            SeatPosition.FOUR_REAR_RIGHT -> {
                vector.multiply(1.3).rotateAroundY(Math.toRadians(-158.0))
                return location.clone().add(vector)
            }
        }

    }
}