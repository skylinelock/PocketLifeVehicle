package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.CustomEntityTypes
import net.minecraft.server.v1_14_R1.*
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */
class SeatArmorStand : EntityArmorStand {
    private var vehicle: Vehicle? = null
    private var position: SeatPosition? = null
    private var control: SeatPositionControl? = null

    constructor(entityTypes: EntityTypes<out EntityArmorStand>, world: World) : super(entityTypes, world) {
        this.killEntity()
    }

    constructor(world: World, x: Double, y: Double, z: Double) : super(EntityTypes.a(CustomEntityTypes.VEHICLE_SEAT.key).get() as EntityTypes<SeatArmorStand>, world) {
        super.setPosition(x, y, z)
        val nbt = NBTTagCompound()
        nbt.setBoolean("NoBasePlate", true)
        nbt.setBoolean("Invulnerable", true)
        nbt.setBoolean("PersistenceRequired", true)
        nbt.setBoolean("NoGravity", false)
        nbt.setBoolean("Invisible", true)
        nbt.setBoolean("Marker", false)
        this.a(nbt)
    }

    fun assemble(vehicle: Vehicle, position: SeatPosition) {
        this.vehicle = vehicle
        this.position = position
        control = SeatPositionControl()
        val center = vehicle.location
        val loc = control!!.calculate(center, position)
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
        val loc = control!!.calculate(vehicle!!.location, position!!)
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
        get() = position != null && control != null

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
}