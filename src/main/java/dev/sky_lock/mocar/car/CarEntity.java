package dev.sky_lock.mocar.car;

import dev.sky_lock.mocar.MoCar;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * @author sky_lock
 */

public class CarEntity extends EntityArmorStand {
    private final Car car;

    public CarEntity(World world, Car car) {
        super(world);
        this.car = car;

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("NoBasePlate", true);
        nbt.setBoolean("invulnerable", true);
        nbt.setBoolean("PersistenceRequired", true);
        nbt.setBoolean("ShowArms", true);
        nbt.setBoolean("NoGravity", false);
        nbt.setBoolean("Invisible", true);
        this.a(nbt);
        this.setSlot(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.POWERED_RAIL)));
        this.getBukkitEntity().setMetadata("mocar-as", new FixedMetadataValue(MoCar.getInstance(), null));
    }

    /*//Update
    @Override
    public void a(float side, float updown, float forward) {

    }*/

    @Override
    public void a(float sideMot, float f1, float forMot) {
        car.setLocation(getBukkitEntity().getLocation());

        this.P = 1.0F;
        if (passengers != null && !passengers.isEmpty()) {
            EntityLiving passenger = (EntityLiving) passengers.get(0);
            this.fallDistance = 0.0F;

            sideMot = passenger.be * 0.5F;
            forMot = passenger.bg;

            if (sideMot > 0.0F) {
                this.yaw = passenger.yaw + 1F;
            } else if (sideMot < 0.0F) {
                this.yaw = passenger.yaw - 1F;
            } else {
                this.yaw = passenger.yaw;
            }

            sideMot = 0.0F;
            this.lastYaw = this.yaw;
            this.pitch = passenger.pitch * 0.5F;
            setYawPitch(this.yaw, this.pitch);
            this.aN = this.yaw;
            this.aP = this.aQ;

            if (forMot <= 0.0F) {
                forMot *= 0.25F;// Make backwards slower
            }

            //this.Q = ai.climbingHeight.toFloat();
            this.aR = this.cy() * 0.1f;

            this.aF = this.aG;
            double d0 = this.locX - this.lastX;
            double d1 = this.locZ - this.lastZ;
            double f4 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0f;

            if (f4 > 1.0f) {
                f4 = 1.0f;
            }

            this.aG += (f4 - this.aG) * 0.4f;
            this.aH += this.aG;
        }
        this.k(0.20f);
        super.a(sideMot, f1, forMot);
    }

    @Override
    public CraftEntity getBukkitEntity() {
        if (this.bukkitEntity == null || !(this.bukkitEntity instanceof CraftCar)) {
            this.bukkitEntity = new CraftCar((CraftServer) Bukkit.getServer(), this);
        }
        return super.getBukkitEntity();
    }
}
