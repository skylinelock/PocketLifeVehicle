package dev.sky_lock.pocketlifevehicle.vehicle

import net.minecraft.server.v1_14_R1.*
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */
class SeatArmorStand(world: World, x: Double, y: Double, z: Double) : EntityArmorStand(world, x, y, z) {
    private var car: Car? = null
    private var position: SeatPosition? = null
    private var control: SeatPositionControl? = null
    fun assemble(car: Car, position: SeatPosition) {
        this.car = car
        this.position = position
        control = SeatPositionControl()
        val center = car.location
        val loc = control!!.calculate(center, position)
        setLocation(loc.x, center.y - 1.675 + car.model.height, loc.z, center.yaw, center.pitch)
    }

    //降りた時
    override fun removePassenger(entity: Entity): Boolean {
        if (!isCarSheet) {
            super.removePassenger(entity)
            return true
        }
        super.removePassenger(entity)
        if (entity.bukkitEntity !is Player) {
            return true
        }
        val player = entity.bukkitEntity as Player
        player.sendActionBar("")
        return true
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
        if (car!!.driver == null) {
            synchronize()
            return
        }
        val passenger = passengers[0] as EntityLiving
        if (passenger !is EntityPlayer) {
            synchronize()
            return
        }
        car!!.meterPanel.display(((passenger as EntityHuman).bukkitEntity as Player))
        synchronize()
    }

    val isDriverSheet: Boolean
        get() = position === SeatPosition.ONE_DRIVER || position === SeatPosition.TWO_DRIVER || position === SeatPosition.FOUR_DRIVER

    private fun synchronize() {
        val loc = control!!.calculate(car!!.location, position!!)
        locX = loc.x
        locY = car!!.location.y - 1.675 + car!!.model.height
        locZ = loc.z
        setPosition(locX, locY, locZ)
        yaw = car!!.location.yaw
        lastYaw = yaw
        pitch = car!!.location.pitch
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
}