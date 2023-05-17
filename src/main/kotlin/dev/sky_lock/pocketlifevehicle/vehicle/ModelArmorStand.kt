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
        setMaxUpStep(1.126F)
        this.craftAttributes.registerAttribute(Attribute.GENERIC_MAX_HEALTH)
        this.craftAttributes.registerAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)
        this.craftAttributes.registerAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
        this.craftAttributes.registerAttribute(Attribute.GENERIC_ARMOR)
        this.craftAttributes.registerAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS)
    }

    fun assemble(status: VehicleStatus) {
        this.status = status
        val armorStand = bukkitEntity as CraftArmorStand
        armorStand.rightArmPose = EulerAngle.ZERO
        val model = status.model
        val modelOption = model.modelOption
        armorStand.setItem(modelOption.position.slot, model.itemStack)
        armorStand.isSmall = !modelOption.isBig
        this.refreshDimensions()
    }

    fun setDriverSeat(driverSeat: SeatArmorStand) {
        this.driverSeat = driverSeat
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

    // 毎tick呼び出される
    // 引数vec3のxyzは絶対座標で、相対的な動きはしない
    override fun travel(vec3: Vec3) {
        if (status == null) {
            super.travel(vec3)
            return
        }
        val status = status!!
        status.updateLocation(location)

        val driverSeat = driverSeat!!
        if (status.isUndrivable) {
            status.engine.stop()
            super.travel(vec3)
            return
        }
        // TODO: 水出たときに音を鳴らす
        if (isInWater || isInLava) {
            status.engine.stop()
            super.travel(vec3)
            return
        }
        if (tickCount % 2 == 0) {
            playEngineSound(status)
        }
        if (driverSeat.passengers.isEmpty()) {
            status.engine.stop()
            super.travel(vec3)
            return
        }

        val driver = driverSeat.passengers[0].bukkitEntity as CraftPlayer
        val player = driver.handle

        val sidewaysSpeed = player.xxa
        val forwardSpeed = player.zza
        val spaced = player.jumping

        status.steering.update(driver, sidewaysSpeed)
        status.engine.update(sidewaysSpeed, forwardSpeed)
        this.speed = status.engine.currentSpeed

        if (spaced) {
            yRot = status.yaw
            yRotO = this.yRot
            xRot = 0.0f
            this.setRot(yRot, xRot)
            this.yBodyRot = this.yRot
            this.yHeadRot = this.yBodyRot
            super.travel(vec3.add(Vec3(sidewaysSpeed.toDouble(), 0.0, forwardSpeed.toDouble())))
        } else {
            yRot = status.yaw
            yRotO = this.yRot
            xRot = 0.0f
            this.setRot(yRot, xRot)
            this.yBodyRot = this.yRot
            this.yHeadRot = this.yBodyRot
            // Z方向（yawの進行方向）に進ませる。
            // vec3は
            super.travel(vec3.add(Vec3(0.0, 0.0, 1.0)))
         }
    }

/*    override fun handleRelativeFrictionAndCalculateMovement(movementInput: Vec3, slipperiness: Float): Vec3 {
        val driverSeat = driverSeat!!
        if (driverSeat.passengers.isEmpty()) {
            return deltaMovement
        }
        val driver = driverSeat.passengers[0].bukkitEntity as CraftPlayer
        val player = driver.handle

        if (player.jumping) {
            val iceFriction = 0.98F
            val frictionalSpeed = this.speed * (0.21600002F / iceFriction * iceFriction * iceFriction)
            moveRelative(frictionalSpeed, movementInput)
            move(MoverType.SELF, movementInput)
            return deltaMovement
        }
        return super.handleRelativeFrictionAndCalculateMovement(movementInput, slipperiness)
    }*/

    override fun tick() {
        super.tick()
        val status = status!!
        status.updateLocation(location)
    }

}