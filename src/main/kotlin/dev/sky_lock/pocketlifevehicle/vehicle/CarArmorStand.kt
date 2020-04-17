package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.task.BurnExplosionTask
import dev.sky_lock.pocketlifevehicle.task.SubmergedMessageTask
import net.minecraft.server.v1_14_R1.*
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftArmorStand
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.util.EulerAngle

/**
 * @author sky_lock
 */
class CarArmorStand : EntityArmorStand {
    private var car: Car? = null

    constructor(entitytypes: EntityTypes<out EntityArmorStand>, world: World) : super(entitytypes, world) {}
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
        K = 1.0f
    }

    fun assemble(car: Car) {
        this.car = car
        val armorstand: ArmorStand = bukkitEntity as CraftArmorStand
        armorstand.rightArmPose = EulerAngle.ZERO
        val model = car.model
        armorstand.setItem(model.itemOption.position.slot, model.itemStack)
        armorstand.isSmall = !model.isBig
        // armorstand.setMetadata("mocar-as", new FixedMetadataValue(PLVehicle.getInstance(), null));
        car.soundTask.start()
    }

    //TODO: 当たり判定
    //    @Override
    //    public EntitySize a(EntityPose entitypose) {
    //        EntitySize size = this.getEntityType().j();
    //        CollideBox collideBox = this.car.getModel().getCollideBox();
    //        float widthScale = collideBox.getBaseSide() / size.width;
    //        float heightScale = collideBox.getHeight() / size.height;
    //        return this.getEntityType().j().a(widthScale, heightScale);
    //    }
    val location: Location
        get() = bukkitEntity.location

    override fun killEntity() {
        car!!.soundTask.stop()
        super.killEntity()
    }

    //ツタ、はしご、足場ブロックを登れなくする
    override fun isClimbing(): Boolean {
        return false
    }

    //足音がなるかどうか
    override fun isSilent(): Boolean {
        return isCarArmorStand
    }

    //水に入った時
    override fun az() {
        if (!isCarArmorStand) {
            super.au()
            return
        }
        super.au()
        SubmergedMessageTask().run(car!!)
        car!!.soundTask.stop()
    }

    override fun burn(i: Float) {
        if (!isCarArmorStand) {
            super.burn(i)
            return
        }
        super.burn(i)
        if (!car!!.isBeginExplode) {
            BurnExplosionTask().run(car!!)
            car!!.isBeginExplode = true
        }
    }

    override fun e(vec3d: Vec3D) {
        if (car!!.passengers.isEmpty() || !car!!.driver.isPresent || this.isInWater || inLava) {
            car!!.engine.stop()
            super.e(vec3d)
            return
        }
        car!!.driver.ifPresent { driver: Player ->
            val player = (driver as CraftPlayer).handle
            val sideIn = player.bb
            val forwardIn = player.bd
            if (sideIn < 0.0f) {
                car!!.steering.right(driver)
            } else if (sideIn > 0.0f) {
                car!!.steering.left(driver)
            }
            car!!.engine.update(forwardIn)
            car!!.engine.consumeFuel(sideIn)
        }
        fallDistance = 0.0f
        yaw = car!!.status.yaw
        lastYaw = yaw
        pitch = 0.0f
        setYawPitch(yaw, pitch)
        // this.aQ = this.yaw;
        this.o(car!!.engine.currentSpeed)
        super.e(vec3d.e(Vec3D(0.0, 1.0, 3.0)))
        car!!.status.location = location
    }

//    override fun getBukkitEntity(): CraftEntity {
//        return CraftCar(Bukkit.getServer() as CraftServer, this)
//    }

    private val isCarArmorStand: Boolean
        get() = car!!.model != null && car!!.status != null

/*    inner class CraftCar internal constructor(server: CraftServer, entity: EntityArmorStand) : CraftArmorStand(server, entity) {
        override fun getHandle(): CarArmorStand {
            return super.getHandle() as CarArmorStand
        }
    }*/
}