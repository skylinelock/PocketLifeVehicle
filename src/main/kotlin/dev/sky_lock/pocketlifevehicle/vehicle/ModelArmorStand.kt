package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.CustomEntityTypes
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

    constructor(entityTypes: EntityTypes<out EntityArmorStand>, world: World) : super(entityTypes, world) {
        this.killEntity()
    }

    constructor(world: World, x: Double, y: Double, z: Double, yaw: Float) : super(EntityTypes.a(CustomEntityTypes.VEHICLE_MODEL.key).get() as EntityTypes<SeatArmorStand>, world) {
        super.setPosition(x, y, z)
        super.setYawPitch(yaw, 0.0F)
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
        val armorStand: ArmorStand = bukkitEntity as CraftArmorStand
        armorStand.rightArmPose = EulerAngle.ZERO
        val model = vehicle.model
        armorStand.setItem(model.itemOption.position.slot, model.itemStack)
        armorStand.isSmall = !model.isBig
        this.updateSize()
        vehicle.engineSound.start()
    }

    val location: Location
        get() = bukkitEntity.location

    override fun killEntity() {
        if (vehicle != null) {
            vehicle!!.engineSound.isCancelled = true
        }
        super.killEntity()
    }

    override fun isClimbing(): Boolean {
        return false
    }

    override fun isSilent(): Boolean {
        return true
    }

    override fun burn(i: Float) {
        super.burn(i)
        if (!vehicle!!.isBeginExplode) {
            BurnExplosionTask().run(vehicle!!)
            vehicle!!.isBeginExplode = true
        }
    }

    // enterWater
    override fun az() {
        super.az()
        SubmergedMessageTask().run(vehicle!!)
        vehicle!!.engineSound.isCancelled = true
    }

    // applyPoseToSize
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

    // movementTick
    override fun e(vec3d: Vec3D) {
        if (vehicle == null) {
            super.e(vec3d)
            return
        }
        val vehicle = vehicle!!
        if (vehicle.isUndrivable || vehicle.passengers.isEmpty() ||
                vehicle.driver == null || this.isInWater || inLava) {
            vehicle.engine.stop()
            vehicle.engineSound.pitch = 0.0f
            vehicle.engineSound.location = this.location
            super.e(vec3d)
            return
        }
        vehicle.driver.let { driver ->
            val player = (driver as CraftPlayer).handle
            val sideIn = player.bb
            val forwardIn = player.bd
            if (sideIn < 0.0f) {
                vehicle.steering.right(driver)
            } else if (sideIn > 0.0f) {
                vehicle.steering.left(driver)
            }
            vehicle.engine.update(forwardIn)
            vehicle.engine.consumeFuel(sideIn)
        }

        // yawとpitchを設定
        this.yaw = vehicle.status.yaw
        this.lastYaw = this.yaw
        this.pitch = 0.0f
        this.setYawPitch(yaw, pitch)
        this.aK = this.yaw;
        this.aM = this.aK

        this.o(vehicle.engine.currentSpeed)
        super.e(vec3d.e(Vec3D(0.0, 1.0, 3.0)))

        vehicle.status.location = location
        vehicle.engineSound.location = this.location
        vehicle.engineSound.pitch = vehicle.status.speed.approximate() / vehicle.model.spec.maxSpeed.value
    }
}