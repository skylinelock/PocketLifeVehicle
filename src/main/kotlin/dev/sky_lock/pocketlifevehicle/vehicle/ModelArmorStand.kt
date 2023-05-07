package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.VehicleEntityType
import dev.sky_lock.pocketlifevehicle.task.BurnExplosionTask
import dev.sky_lock.pocketlifevehicle.task.SubmergedMessageTask
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.EntityDimensions
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Pose
import net.minecraft.world.entity.ai.attributes.AttributeMap
import net.minecraft.world.entity.decoration.ArmorStand
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftArmorStand
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer
import org.bukkit.util.EulerAngle

/**
 * @author sky_lock
 */
class ModelArmorStand : ArmorStand {
    private var vehicle: Vehicle? = null
    constructor(entityTypes: EntityType<out ArmorStand>, world: Level) : super(entityTypes, world) {
        this.kill()
    }

    constructor(
        world: Level,
        x: Double,
        y: Double,
        z: Double,
        yaw: Float
    ) : super(VehicleEntityType.MODEL.type(), world) {
        super.setPos(x, y, z)
        super.setRot(yaw, 0.0F)
        this.readAdditionalSaveData(EntityVehicleHelper.modelNBT())
        // Entity#K = maxUpStep
        setMaxUpStep(1.0f)
        this.craftAttributes.registerAttribute(Attribute.GENERIC_MAX_HEALTH)
        this.craftAttributes.registerAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)
        this.craftAttributes.registerAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
        this.craftAttributes.registerAttribute(Attribute.GENERIC_ARMOR)
        this.craftAttributes.registerAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS)
    }

    fun assemble(vehicle: Vehicle) {
        this.vehicle = vehicle
        val armorStand = bukkitEntity as CraftArmorStand
        armorStand.rightArmPose = EulerAngle.ZERO
        val model = vehicle.model
        val modelOption = model.modelOption
        armorStand.setItem(modelOption.position.slot, model.itemStack)
        armorStand.isSmall = !modelOption.isBig
        this.refreshDimensions()
        if (vehicle.shouldPlaySound) {
            vehicle.sound.start()
        }
    }

    val location: Location
        get() = bukkitEntity.location

    override fun getAttributes() : AttributeMap {
        return AttributeMap(LivingEntity.createLivingAttributes().build())
    }

    override fun kill() {
        if (vehicle != null) {
            vehicle!!.sound.isCancelled = true
        }
        super.kill()
    }

    override fun onClimbable() = false

    override fun isSilent() = true

    override fun hurt(source: DamageSource, amount: Float): Boolean {
        super.hurt(source, amount)
        if (!(source.`is`(DamageTypes.IN_FIRE) && source.`is`(DamageTypes.LAVA) && source.`is`(DamageTypes.ON_FIRE))) {
            return false
        }
        if (!vehicle!!.isBeginExplode) {
            BurnExplosionTask().run(vehicle!!)
            vehicle!!.isBeginExplode = true
        }
        return false
    }

    // Entity#az() = doWaterSplashEffect()
    override fun doWaterSplashEffect() {
        super.doWaterSplashEffect()
        SubmergedMessageTask().run(vehicle!!)
        vehicle!!.sound.isCancelled = true
    }

    // applyPoseToSize
    override fun getDimensions(entityPose: Pose): EntityDimensions {
        if (vehicle == null) {
            return super.getDimensions(entityPose)
        }
        val size = this.type.dimensions
        val boxSize = vehicle!!.model.size
        val widthScale = boxSize.baseSide / size.width
        val heightScale = boxSize.height / size.height
        return this.type.dimensions.scale(widthScale, heightScale)
    }

    // EntityLiving#e(Vec3D) = travel(Vec3D)
    override fun travel(vec3: Vec3) {
        if (vehicle == null) {
            super.travel(vec3)
            return
        }
        val vehicle = vehicle!!
        if (vehicle.isUndrivable || vehicle.passengers.isEmpty() ||
            vehicle.driver == null || isInWater || isInLava
        ) {
            vehicle.engine.stop()
            vehicle.location = location
            vehicle.sound.pitch = 0.0f
            vehicle.sound.location = this.location
            super.travel(vec3)
            return
        }
        val driver = vehicle.driver

        if (driver != null) {
            val player = (driver as CraftPlayer).handle
            val sideIn = player.xxa
            val forIn = player.zza
            if (sideIn < 0.0f) {
                vehicle.steering.right(driver)
            } else if (sideIn > 0.0f) {
                vehicle.steering.left(driver)
            }
            vehicle.engine.update(sideIn, forIn)
        }

        // yawとpitchを設定
        yRot = vehicle.yaw
        yRotO = this.yRot
        xRot = 0.0f
        this.setRot(yRot, xRot)
        this.yBodyRot = this.yRot
        this.yHeadRot = this.yBodyRot

        this.speed = vehicle.engine.currentSpeed
        // InputのZ方向に進ませる。後に単位ベクトルに置き換えられるのでZは1.0で良い。
        super.travel(vec3.add(Vec3(0.0, 0.0, 1.0)))

        val speed = vehicle.engine.speed
        vehicle.location = location
        vehicle.sound.location = this.location
        vehicle.sound.pitch = speed.approximate() / vehicle.model.spec.maxSpeed.value
    }
}