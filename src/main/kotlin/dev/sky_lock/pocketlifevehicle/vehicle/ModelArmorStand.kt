package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.CustomEntityTypes
import dev.sky_lock.pocketlifevehicle.task.BurnExplosionTask
import dev.sky_lock.pocketlifevehicle.task.SubmergedMessageTask
import net.minecraft.server.v1_14_R1.*
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftArmorStand
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer
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
        this.a(EntityVehicleHelper.modelNBT())
        //乗れるブロックの高さ
        this.K = 1.0f
    }

    fun assemble(vehicle: Vehicle) {
        this.vehicle = vehicle
        val armorStand = bukkitEntity as CraftArmorStand
        armorStand.rightArmPose = EulerAngle.ZERO
        val model = vehicle.model
        val modelOption = model.modelOption
        armorStand.setItem(modelOption.position.slot, model.itemStack)
        armorStand.isSmall = !modelOption.isBig
        this.updateSize()
        if (vehicle.model.flag.engineSound) {
            vehicle.sound.start()
        }
    }

    val location: Location
        get() = bukkitEntity.location

    override fun killEntity() {
        if (vehicle != null) {
            vehicle!!.sound.isCancelled = true
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
        vehicle!!.sound.isCancelled = true
    }

    // applyPoseToSize
    override fun a(entityPose: EntityPose): EntitySize {
        if (vehicle == null) {
            return super.a(entityPose)
        }
        val size = this.entityType.k()
        val boxSize = vehicle!!.model.size
        val widthScale = boxSize.baseSide / size.width
        val heightScale = boxSize.height / size.height
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
            vehicle.location = location
            vehicle.sound.pitch = 0.0f
            vehicle.sound.location = this.location
            super.e(vec3d)
            return
        }
        vehicle.driver.let { driver ->
            val player = (driver as CraftPlayer).handle
            val sideIn = player.bb
            val forIn = player.bd
            if (sideIn < 0.0f) {
                vehicle.steering.right(driver)
            } else if (sideIn > 0.0f) {
                vehicle.steering.left(driver)
            }
            vehicle.engine.update(sideIn, forIn)
        }

        // yawとpitchを設定
        this.yaw = vehicle.yaw
        this.lastYaw = this.yaw
        this.pitch = 0.0f
        this.setYawPitch(yaw, pitch)
        this.aK = this.yaw
        this.aM = this.aK

        this.o(vehicle.engine.currentSpeed)
        super.e(vec3d.e(Vec3D(0.0, 0.0, 3.0)))

        val speed = vehicle.engine.speed
        vehicle.location = location
        vehicle.sound.location = this.location
        vehicle.sound.pitch = speed.approximate() / vehicle.model.spec.maxSpeed.value
    }
}