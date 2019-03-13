package dev.sky_lock.mocar.car;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.packet.ActionBar;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.math.BigDecimal;

/**
 * @author sky_lock
 */

public class CarArmorStand extends EntityArmorStand {
    private final CarEntity carEntity;
    private float steer_yaw;
    private float currentSpeed;
    private float acceleration;

    public CarArmorStand(World world, CarEntity carEntity) {
        super(world);
        this.carEntity = carEntity;

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

    public CarEntity getCarEntity() {
        return carEntity;
    }

    @Override
    protected void p(Entity entity) {
        super.p(entity);
        if (!(entity.getBukkitEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) entity.getBukkitEntity();
        ActionBar.sendPacket(player, "");
    }

    //ツタとかはしごとかを登れなくする
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
        setSize(2.5F, 2.5F);
    }


    @Override
    public void a(float sideMot, float f1, float forMot) {
        carEntity.setLocation(getBukkitEntity().getLocation());
        if (this.isInWater() || this.au()) {
            this.killEntity();
            return;
        }
        if (passengers == null || passengers.isEmpty()) {
            super.a(sideMot, f1, forMot);
            carEntity.setSpeed(BigDecimal.ZERO);
            return;
        }

        EntityLiving passenger = (EntityLiving) passengers.get(0);
        if (!(passenger instanceof EntityPlayer)) {
            super.a(sideMot, f1, forMot);
            return;
        }

        carEntity.useFuel(0.05f);

        StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.GREEN);
        float fuelRate = carEntity.getFuel() / carEntity.getModel().getMaxFuel();
        int filledRate = Math.round(20 * fuelRate);
        for (int i = 0; i < filledRate; i++) {
            builder.append("█");
        }
        builder.append(ChatColor.RED);
        for (int i = 0; i < 20 - filledRate; i++) {
            builder.append("█");
        }
        builder.append(" ");
        if (Math.round(carEntity.getFuel()) == 0) {
            builder.append(ChatColor.RED);
            builder.append("Empty");
        } else {
            builder.append(ChatColor.DARK_GREEN);
            builder.append(Math.round(carEntity.getFuel()));
        }
        builder.append(" / ");
        builder.append(Math.round(carEntity.getModel().getMaxFuel()));
        ActionBar.sendPacket(((EntityPlayer) passenger).getBukkitEntity(), builder.toString());

        float sideInput = passenger.be;
        float forInput = passenger.bg;

        sideMot = 0.0f;
        forMot = 3.0f;

        this.fallDistance = 0.0F;

        if (sideInput < 0.0F) {
            steer_yaw += 5.5F;
        } else if (sideInput > 0.0F) {
            steer_yaw -= 5.5F;
        }

        this.yaw = steer_yaw;
        this.lastYaw = this.yaw;
        this.pitch = passenger.pitch * 0.5F;
        setYawPitch(this.yaw, this.pitch);
        this.aN = this.yaw;
        this.aP = this.aQ;

        //乗れるブロックの高さ
        this.P = 1.0F;

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
        k(carEntity.calculateSpeed(forInput));
        super.a(sideMot, f1, forMot);
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
}