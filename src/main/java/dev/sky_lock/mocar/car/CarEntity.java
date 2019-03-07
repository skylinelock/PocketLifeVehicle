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
    private float steer_yaw;
    private boolean isRiding;

    public CarEntity(World world, Car car) {
        super(world);
        this.car = car;
        //当たり判定

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("NoBasePlate", true);
        nbt.setBoolean("Invulnerable", true);
        nbt.setBoolean("PersistenceRequired", true);
        nbt.setBoolean("ShowArms", true);
        nbt.setBoolean("NoGravity", false);
        nbt.setBoolean("Invisible", true);
        nbt.setBoolean("Marker", false);
        this.a(nbt);
        this.setSlot(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.POWERED_RAIL)));
        this.getBukkitEntity().setMetadata("mocar-as", new FixedMetadataValue(MoCar.getInstance(), null));
    }


    public void setRiding(boolean riding) {
        this.isRiding = riding;
    }

    //ツタとかはしごとか
    @Override
    public boolean m_() {
        return false;
    }

    //足音がなるかどうか
    @Override
    public boolean isSilent() {
        return true;
    }

    //tick毎に呼ばれる
    @Override
    public void B_() {
        super.B_();
        //当たり判定
        setSize(4.0F, 5.0F);
    }

    @Override
    public void a(float sideMot, float f1, float forMot) {
        car.setLocation(getBukkitEntity().getLocation());

        if (this.isInWater() || this.au()) {
            this.killEntity();
            return;
        }

        //乗れるブロックの高さ
        this.P = 1.0F;
        if (passengers != null && !passengers.isEmpty()) {
            EntityLiving passenger = (EntityLiving) passengers.get(0);
            if (!isRiding) {
                passenger.yaw = this.yaw;
                isRiding = true;
            }
            this.fallDistance = 0.0F;

            forMot = passenger.bg;
            this.yaw = steer_yaw;

            if (passenger.be < 0.0F) {
                steer_yaw += 5.5F;
            } else if (passenger.be > 0.0F) {
                steer_yaw -= 5.5F;
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
        this.k(0.50f);
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
