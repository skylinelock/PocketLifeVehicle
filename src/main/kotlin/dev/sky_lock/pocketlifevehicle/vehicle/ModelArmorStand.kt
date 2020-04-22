package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.task.BurnExplosionTask
import dev.sky_lock.pocketlifevehicle.task.SubmergedMessageTask
import net.minecraft.server.v1_14_R1.*
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftArmorStand
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer
import org.bukkit.entity.ArmorStand
import org.bukkit.util.EulerAngle

/**
 * @author sky_lock
 */
class ModelArmorStand : EntityArmorStand {
    private var vehicle: Vehicle? = null

    constructor(entityTypes: EntityTypes<out EntityArmorStand>, world: World) : super(entityTypes, world)
    constructor(world: World, x: Double, y: Double, z: Double) : super(world, x, y, z) {
        val nbt = NBTTagCompound()
        nbt.setBoolean("NoBasePlate", true)
        nbt.setBoolean("Invulnerable", true)
        nbt.setBoolean("PersistenceRequired", true)
        nbt.setBoolean("NoGravity", false)
        nbt.setBoolean("Invisible", true)
        nbt.setBoolean("Marker", false) // ArmorStand has a very small collision box when true
        nbt.setBoolean("Small", true)
        this.a(nbt)
        //乗れるブロックの高さ
        this.K = 1.0f
    }

    fun assemble(vehicle: Vehicle) {
        this.vehicle = vehicle
        val armorstand: ArmorStand = bukkitEntity as CraftArmorStand
        armorstand.rightArmPose = EulerAngle.ZERO
        val model = vehicle.model
        armorstand.setItem(model.itemOption.position.slot, model.itemStack)
        armorstand.isSmall = !model.isBig
        this.updateSize()
        vehicle.engineSound.start()
    }

    override fun a(entityPose: EntityPose): EntitySize {
        if (vehicle == null) {
            return super.a(entityPose)
        }
        val size = this.entityType.k()
        val collideBox = vehicle!!.model.collideBox
        val widthScale = collideBox.baseSide / size.width
        val heightScale = collideBox.height / size.height
        return entityType.k().a(widthScale, heightScale)
    }

    val location: Location
        get() = bukkitEntity.location

    override fun killEntity() {
        vehicle!!.engineSound.stop()
        super.killEntity()
    }

    //ツタ、はしご、足場ブロックを登れなくする
    override fun isClimbing(): Boolean {
        return false
    }

    //足音がなるかどうか
    override fun isSilent(): Boolean {
        return true
    }

    //水に入った時
    override fun az() {
        super.au()
        SubmergedMessageTask().run(vehicle!!)
        vehicle!!.engineSound.stop()
    }

    override fun burn(i: Float) {
        super.burn(i)
        if (!vehicle!!.isBeginExplode) {
            BurnExplosionTask().run(vehicle!!)
            vehicle!!.isBeginExplode = true
        }
    }

    override fun e(vec3d: Vec3D) {
        if (vehicle!!.passengers.isEmpty() || vehicle!!.driver == null || this.isInWater || inLava) {
            vehicle!!.engine.stop()
            super.e(vec3d)
            return
        }
        vehicle!!.driver.let { driver ->
            val player = (driver as CraftPlayer).handle
            val sideIn = player.bb
            val forwardIn = player.bd
            if (sideIn < 0.0f) {
                vehicle!!.steering.right(driver)
            } else if (sideIn > 0.0f) {
                vehicle!!.steering.left(driver)
            }
            vehicle!!.engine.update(forwardIn)
            vehicle!!.engine.consumeFuel(sideIn)
        }
        fallDistance = 0.0f
        yaw = vehicle!!.status.yaw
        lastYaw = yaw
        pitch = 0.0f
        setYawPitch(yaw, pitch)
        // this.aQ = this.yaw;
        this.o(vehicle!!.engine.currentSpeed)
        super.e(vec3d.e(Vec3D(0.0, 1.0, 3.0)))
        vehicle!!.status.location = location
    }

//    override fun getBukkitEntity(): CraftEntity {
//        return CraftCar(Bukkit.getServer() as CraftServer, this)
//    }


/*    inner class CraftCar internal constructor(server: CraftServer, entity: EntityArmorStand) : CraftArmorStand(server, entity) {
        override fun getHandle(): CarArmorStand {
            return super.getHandle() as CarArmorStand
        }
    }*/
}