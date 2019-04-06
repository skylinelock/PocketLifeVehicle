
package dev.sky_lock.mocar.car;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.packet.AnimationPacket;
import dev.sky_lock.mocar.task.BurnExplosionTask;
import dev.sky_lock.mocar.task.SubmergedMessageTask;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.CraftServer;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MainHand;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * @author sky_lock
 */

public class CarArmorStand extends EntityArmorStand {

    private Car car;

    public CarArmorStand(World world) {
        super(world);
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
        this.Q = 1.0F;
        this.setSize(6.0F, 6.0F);
    }

    void assemble(Car car) {
        this.car = car;
        this.setSlot(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(car.getModel().getItem().getStack(car.getModel().getName())));
        this.getBukkitEntity().setMetadata("mocar-as", new FixedMetadataValue(MoCar.getInstance(), null));
        car.getSound().start();
    }

    @Override
    public void tick() {
        super.tick();
        setSize(6.0F, 6.0F);
    }

    public Location getLocation() {
        return getBukkitEntity().getLocation();
    }

    @Override
    public void killEntity() {
        super.killEntity();
        car.getSound().stop();
    }

    //ツタとかはしごとかを登れなくする
    @Override
    public boolean z_() {
        return false;
    }

    //足音がなるかどうか
    @Override
    public boolean isSilent() {
        return isCarArmorStand();
    }

    //水に入った時
    @Override
    protected void au() {
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
    public void a(float sideMot, float f1, float forMot) {
        if (car.getPassengers().isEmpty() || !car.getDriver().isPresent()) {
            car.getEngine().stop();
            super.a(sideMot, f1, forMot);
            return;
        }

        sideMot = 0.0f;
        forMot = 3.0f;

        car.getDriver().ifPresent(driver -> {
            EntityPlayer player = ((CraftPlayer) driver).getHandle();
            float sideInput = player.bh;
            float forInput = player.bj;

            if (sideInput < 0.0F) {
                car.getSteering().right();
                raiseLeftArm(driver);
            } else if (sideInput > 0.0F) {
                car.getSteering().left();
                raiseRightArm(driver);
            }

            car.getEngine().update(forInput);
            car.getEngine().consumeFuel(sideInput);
        });

        this.fallDistance = 0.0F;

        this.yaw = car.getStatus().getYaw();
        this.lastYaw = this.yaw;
        this.pitch = 0.0F;
        setYawPitch(this.yaw, this.pitch);
        this.aQ = this.yaw;
/*        this.aS = this.aQ;

        this.aU = this.cK() * 0.1f;

        this.aI = this.aJ;
        double d0 = this.locX - this.lastX;
        double d1 = this.locZ - this.lastZ;
        double f4 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0f;

        if (f4 > 1.0f) {
            f4 = 1.0f;
        }

        this.aJ += (f4 - this.aJ) * 0.4f;
        this.aK += this.aJ;*/
        this.o(car.getEngine().getCurrentSpeed());
        super.a(sideMot, f1, forMot);
        car.getStatus().setLocation(getLocation());
    }

    private void raiseLeftArm(Player player) {
        if (player.getMainHand() == MainHand.RIGHT) {
            raiseOffhand(player.getEntityId());
        } else {
            raiseMainHand(player.getEntityId());
        }
    }

    private void raiseRightArm(Player player) {
        if (player.getMainHand() == MainHand.RIGHT) {
            raiseMainHand(player.getEntityId());
        } else {
            raiseOffhand(player.getEntityId());
        }
    }

    private void raiseMainHand(int entityID) {
        AnimationPacket packet = new AnimationPacket();
        packet.setEntityID(entityID);
        packet.setAnimation(AnimationPacket.AnimationType.SWING_MAIN_ARM);
        packet.broadCast();
    }

    private void raiseOffhand(int entityID) {
        AnimationPacket packet = new AnimationPacket();
        packet.setEntityID(entityID);
        packet.setAnimation(AnimationPacket.AnimationType.SWING_OFFHAND);
        packet.broadCast();
    }

    @Override
    public CraftEntity getBukkitEntity() {
        if (this.bukkitEntity == null || !(this.bukkitEntity instanceof CraftCar)) {
            this.bukkitEntity = new CraftCar((CraftServer) Bukkit.getServer(), this);
        }
        return super.getBukkitEntity();
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
    }

}