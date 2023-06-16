package dev.sky_lock.pocketlifevehicle.vehicle.entity.component

import dev.sky_lock.pocketlifevehicle.vehicle.entity.EntityVehicle
import dev.sky_lock.pocketlifevehicle.vehicle.entity.VehicleManager
import net.minecraft.core.Rotations
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.EntityDimensions
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Pose
import net.minecraft.world.entity.decoration.ArmorStand
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack

/**
 * @author sky_lock
 */
class ModelArmorStand(entityType: EntityType<ArmorStand>, world: Level) :
    BaseArmorStand(entityType, world) {
    lateinit var entityVehicle: EntityVehicle

    // チャンクロード時に呼ばれる
    override fun load(nbt: CompoundTag) {
        super.load(nbt)
        val v = VehicleManager.findOrNull(uuid)
        if (v == null) {
            discard()
            return
        }
        this.entityVehicle = v

        setDependingValue()
        applyModelSettings()
    }

    fun applyModelSettings() {
        val model = entityVehicle.model
        val modelOption = entityVehicle.model.modelOption
        super.setYRot(entityVehicle.location.yaw)
        super.setYBodyRot(entityVehicle.location.yaw)
        super.setSmall(true)
        super.setMaxUpStep(1.126F)
        super.setItemSlot(modelOption.position.slot, CraftItemStack.asNMSCopy(model.itemStack))
        super.setRightArmPose(Rotations(0F, 0F, 0F))
        super.setSmall(!modelOption.isBig)
    }

    override fun onClimbable() = false

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
        entityVehicle.cancelEngineSound()
    }

    override fun getDimensions(entityPose: Pose): EntityDimensions {
        if (!::entityVehicle.isInitialized) return super.getDimensions(entityPose)
        val size = this.type.dimensions
        val boxSize = entityVehicle.model.size
        val widthScale = boxSize.baseSide / size.width
        val heightScale = boxSize.height / size.height
        return this.type.dimensions.scale(widthScale, heightScale)
    }

    // 毎tick呼び出される
    // 引数vec3のxyzは絶対座標で、相対的な動きはしない
    override fun travel(vec3: Vec3) {
        if (!::entityVehicle.isInitialized) return
        if (entityVehicle.isScrapped) {
            entityVehicle.speedController.zero()
            super.travel(vec3)
            return
        }
        // TODO: 水出たときに音を鳴らす
        if (isInWater || isInLava) {
            entityVehicle.speedController.zero()
            super.travel(vec3)
            return
        }
        if (tickCount % 2 == 0) {
            entityVehicle.playEngineSound()
        }
        val driver = entityVehicle.driver
        if (driver == null) {
            entityVehicle.speedController.zero()
            super.travel(vec3)
            return
        }
        val nmsDriver = (Bukkit.getPlayer(driver) as CraftPlayer).handle

        val sidewaysSpeed = nmsDriver.xxa
        val forwardSpeed = nmsDriver.zza
        val spaced = nmsDriver.jumping

        this.speed = entityVehicle.calculateSpeed(sidewaysSpeed, forwardSpeed, spaced)
        entityVehicle.updateYaw(nmsDriver, sidewaysSpeed)
        turn(entityVehicle.location.yaw)

        if (spaced) {
            super.travel(vec3.add(Vec3(-sidewaysSpeed.toDouble(), 0.0, 1.0)))
        } else {
            // Z方向（yawの進行方向）に進ませる。
            super.travel(vec3.add(Vec3(0.0, 0.0, 1.0)))
        }
    }

    private fun turn(yaw: Float) {
        super.yRotO = yaw
        super.setRot(yaw, 0.0F)
        super.setYBodyRot(yaw)
        super.setYHeadRot(yaw)
    }

    override fun tick() {
        super.tick()
        if (!::entityVehicle.isInitialized) return
        entityVehicle.location = bukkitEntity.location
    }
}