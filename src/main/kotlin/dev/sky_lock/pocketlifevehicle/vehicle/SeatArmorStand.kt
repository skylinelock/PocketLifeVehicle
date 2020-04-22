package dev.sky_lock.pocketlifevehicle.vehicle

import net.minecraft.server.v1_14_R1.*
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */
class SeatArmorStand(world: World, x: Double, y: Double, z: Double) : EntityArmorStand(world, x, y, z) {
    private var vehicle: Vehicle? = null
    private var position: SeatPosition? = null
    private var control: SeatPositionControl? = null

    init {
        val nbt = NBTTagCompound()
        nbt.setBoolean("NoBasePlate", true)
        nbt.setBoolean("Invulnerable", true)
        nbt.setBoolean("PersistenceRequired", true)
        nbt.setBoolean("NoGravity", false)
        nbt.setBoolean("Invisible", true)
        nbt.setBoolean("Marker", false)
        this.a(nbt)
        // this.getBukkitEntity().setMetadata("mocar-as", new FixedMetadataValue(PLVehicle.getInstance(), null));
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
//
//    inner class CraftSeat internal constructor(server: CraftServer, entity: EntityArmorStand) : CraftArmorStand(server, entity) {
//        override fun getHandle(): SeatArmorStand {
//            return super.getHandle() as SeatArmorStand
//        }
//    }
}