
package dev.sky_lock.pocketlifevehicle.vehicle;

import dev.sky_lock.pocketlifevehicle.task.BurnExplosionTask;
import dev.sky_lock.pocketlifevehicle.task.SubmergedMessageTask;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.CraftServer;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;

/**
 * @author sky_lock
 */

public class CarArmorStand extends EntityArmorStand {

    private Car car;

    public CarArmorStand(EntityTypes<? extends EntityArmorStand> entitytypes, World world) {
        super(entitytypes, world);
    }

    public CarArmorStand(World world, double x, double y, double z) {
        super(world, x, y, z);
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("NoBasePlate", true);
        nbt.setBoolean("Invulnerable", true);
        nbt.setBoolean("PersistenceRequired", true);
        nbt.setBoolean("NoGravity", false);
        nbt.setBoolean("Invisible", true);
        nbt.setBoolean("Marker", false); // ArmorStand has a very small collision box when true
        nbt.setBoolean("Small", true);
        this.a(nbt);
        //乗れるブロックの高さ
        this.K = 1.0F;
    }

    void assemble(Car car) {
        this.car = car;
        ArmorStand armorstand = (CraftArmorStand) this.getBukkitEntity();
        armorstand.setRightArmPose(EulerAngle.ZERO);
        armorstand.setItem(car.getModel().getItemOption().getPosition().getSlot(), car.getModel().getItemStack());
        // armorstand.setMetadata("mocar-as", new FixedMetadataValue(PLVehicle.getInstance(), null));
        car.getSound().start();
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

    public Location getLocation() {
        return getBukkitEntity().getLocation();
    }

    @Override
    public void killEntity() {
        super.killEntity();
        car.getSound().stop();
    }

    //ツタ、はしご、足場ブロックを登れなくする
    @Override
    public boolean isClimbing() {
        return false;
    }

    //足音がなるかどうか
    @Override
    public boolean isSilent() {
        return isCarArmorStand();
    }

    //水に入った時
    @Override
    protected void az() {
        if (!isCarArmorStand()) {
            super.au();
            return;
        }
        super.au();
        new SubmergedMessageTask().run(car);
        car.getSound().stop();
    }

    @Override
    protected void burn(float i) {
        if (!isCarArmorStand()) {
            super.burn(i);
            return;
        }
        super.burn(i);
        if (!car.isBeginExplode()) {
            new BurnExplosionTask().run(car);
            car.setBeginExplode(true);
        }
    }

    @Override
    public void e(Vec3D vec3d) {
        if (car.getPassengers().isEmpty() || !car.getDriver().isPresent() || this.isInWater() || this.inLava) {
            car.getEngine().stop();
            super.e(vec3d);
            return;
        }

        car.getDriver().ifPresent(driver -> {
            EntityPlayer player = ((CraftPlayer) driver).getHandle();
            float sideIn = player.bb;
            float forwardIn = player.bd;

            if (sideIn < 0.0F) {
                car.getSteering().right(driver);
            } else if (sideIn > 0.0F) {
                car.getSteering().left(driver);
            }

            car.getEngine().update(forwardIn);
            car.getEngine().consumeFuel(sideIn);
        });

        this.fallDistance = 0.0F;

        this.yaw = car.getStatus().getYaw();
        this.lastYaw = this.yaw;
        this.pitch = 0.0F;
        setYawPitch(this.yaw, this.pitch);
        // this.aQ = this.yaw;

        this.o(car.getEngine().getCurrentSpeed());
        super.e(vec3d.e(new Vec3D(0.0, 1.0, 3.0)));
        car.getStatus().setLocation(getLocation());
    }

    @Override
    public CraftEntity getBukkitEntity() {
        return new CraftCar((CraftServer) Bukkit.getServer(), this);
    }

    private boolean isCarArmorStand() {
        return car.getModel() != null && car.getStatus() != null;
    }

    public class CraftCar extends CraftArmorStand {
        CraftCar(CraftServer server, EntityArmorStand entity) {
            super(server, entity);
        }

        public CarArmorStand getHandle() {
            return (CarArmorStand) super.getHandle();
        }
    }

}