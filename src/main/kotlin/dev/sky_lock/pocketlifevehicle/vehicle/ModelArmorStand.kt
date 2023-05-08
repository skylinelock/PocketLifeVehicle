package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.VehicleEntityType
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleEffects.cancelEngineSound
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleEffects.playEngineSound
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
    private var status: VehicleStatus? = null
    private var driverSeat: SeatArmorStand? = null

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

    fun assemble(status: VehicleStatus, driverSeat: SeatArmorStand) {
        this.status = status
        this.driverSeat = driverSeat
        val armorStand = bukkitEntity as CraftArmorStand
        armorStand.rightArmPose = EulerAngle.ZERO
        val model = status.model
        val modelOption = model.modelOption
        armorStand.setItem(modelOption.position.slot, model.itemStack)
        armorStand.isSmall = !modelOption.isBig
        this.refreshDimensions()
    }

    val location: Location
        get() = bukkitEntity.location

    override fun getAttributes(): AttributeMap {
        return AttributeMap(LivingEntity.createLivingAttributes().build())
    }

    override fun kill() {
        if (status != null) {
            cancelEngineSound(status!!)
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
/*        if (!status!!.isBeginExplode) {
            BurnExplosionTask().run()
            status!!.isBeginExplode = true
        }*/
        return false
    }

    override fun doWaterSplashEffect() {
        super.doWaterSplashEffect()
        // SubmergedMessageTask().run(status)
        cancelEngineSound(status!!)
    }

    override fun getDimensions(entityPose: Pose): EntityDimensions {
        if (status == null) {
            return super.getDimensions(entityPose)
        }
        val size = this.type.dimensions
        val boxSize = status!!.model.size
        val widthScale = boxSize.baseSide / size.width
        val heightScale = boxSize.height / size.height
        return this.type.dimensions.scale(widthScale, heightScale)
    }

    // Playerがマウントしてる間呼び続けられる
    override fun travel(vec3: Vec3) {
        if (status == null) {
            super.travel(vec3)
            return
        }
        val status = status!!
        val driverSeat = driverSeat!!
        if (status.isUndrivable || driverSeat.passengers.isEmpty() || isInWater || isInLava
        ) {
            status.engine.stop()
            super.travel(vec3)
            return
        }
        val driver = driverSeat.passengers[0].bukkitEntity as CraftPlayer
        val player = driver.handle
        val sideIn = player.xxa
        val forIn = player.zza
        if (sideIn < 0.0f) {
            status.steering.right(driver)
        } else if (sideIn > 0.0f) {
            status.steering.left(driver)
        }
        status.engine.update(sideIn, forIn)

        // yawとpitchを設定
        yRot = status.yaw
        yRotO = this.yRot
        xRot = 0.0f
        this.setRot(yRot, xRot)
        this.yBodyRot = this.yRot
        this.yHeadRot = this.yBodyRot

        this.speed = status.engine.currentSpeed
        // InputのZ方向に進ませる。後に単位ベクトルに置き換えられるのでZは1.0で良い。
        super.travel(vec3.add(Vec3(0.0, 0.0, 1.0)))
    }

    override fun tick() {
        super.tick()
        status?.updateLocation(location)
        if (tickCount % 2 == 0) {
            playEngineSound(status!!)
        }
    }

}