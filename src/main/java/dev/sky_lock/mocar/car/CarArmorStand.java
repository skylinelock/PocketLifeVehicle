
package dev.sky_lock.mocar.car;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.task.BurnExplosionTask;
import dev.sky_lock.mocar.task.SubmergedMessageTask;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.CraftServer;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.metadata.FixedMetadataValue;

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
        nbt.setBoolean("Marker", false);
        nbt.setBoolean("Small", true);
        this.a(nbt);
        //乗れるブロックの高さ
        this.K = 1.0F;
    }

    void assemble(Car car) {
        this.car = car;
        this.setSlot(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(car.getModel().getItemStack()));
        this.getBukkitEntity().setMetadata("mocar-as", new FixedMetadataValue(MoCar.getInstance(), null));
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

    @Override
    public void tick() {
        super.tick();
    }

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
            super.a(vec3d);
            return;
        }

/*        sideMot = 0.0f;
        forMot = 3.0f;*/

        car.getDriver().ifPresent(driver -> {
            EntityPlayer player = ((CraftPlayer) driver).getHandle();
            float sideInput = player.bb;
            float forInput = player.bd;

            if (sideInput < 0.0F) {
                car.getSteering().right(driver);
            } else if (sideInput > 0.0F) {
                car.getSteering().left(driver);
            }

            car.getEngine().update(forInput);
            car.getEngine().consumeFuel(sideInput);
        });

        this.fallDistance = 0.0F;

        this.yaw = car.getStatus().getYaw();
        this.lastYaw = this.yaw;
        this.pitch = 0.0F;
        setYawPitch(this.yaw, this.pitch);
/*        this.aQ = this.yaw;*/

        this.o(car.getEngine().getCurrentSpeed());
        super.a(vec3d);
        car.getStatus().setLocation(getLocation());
    }


    @Override
    public CraftEntity getBukkitEntity() {
        return new CraftCar((CraftServer) Bukkit.getServer(), this);
    }

    @Override
    public int getId() {
        return super.getId();
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