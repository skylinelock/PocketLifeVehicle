package dev.sky_lock.mocar.car;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.packet.ActionBar;
import dev.sky_lock.mocar.task.BurnExplosionTask;
import dev.sky_lock.mocar.task.SubmergedMessageTask;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author sky_lock
 */

public class CarArmorStand extends EntityArmorStand {
    private final static Map<UUID, CarArmorStand> carMap = new HashMap<>();
    private final CarModel model;
    private final CarStatus status;
    private float steer_yaw;
    private float currentSpeed;
    private float acceleration;
    private boolean beginExplode = false;

    CarArmorStand(World world, CarModel model, CarStatus status) {
        super(world);
        this.model = model;
        this.status = status;

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("NoBasePlate", true);
        nbt.setBoolean("Invulnerable", true);
        nbt.setBoolean("PersistenceRequired", true);
        nbt.setBoolean("NoGravity", false);
        nbt.setBoolean("Invisible", true);
        nbt.setBoolean("Marker", false);
        nbt.setBoolean("Small", true);
        this.a(nbt);
        this.setSlot(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(model.getItem().getStack(model.getName())));
        this.getBukkitEntity().setMetadata("mocar-as", new FixedMetadataValue(MoCar.getInstance(), null));
    }

    public Location getLocation() {
        return getBukkitEntity().getLocation();
    }

    public CarStatus getStatus() {
        return status;
    }

    public boolean refuel(float fuel) {
        float current = status.getFuel();
        float max = model.getMaxFuel();
        if (current >= max) {
            return false;
        }
        if (current + fuel > max) {
            status.setFuel(max);
            return true;
        }
        status.setFuel(current + fuel);
        return true;
    }

    //降りた時
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

    //水に入った時
    @Override
    protected void ar() {
        super.ar();
        new SubmergedMessageTask().run(this);
    }

    @Override
    protected void burn(float i) {
        super.burn(i);
        if (!beginExplode) {
            new BurnExplosionTask().run(this);
            this.beginExplode = true;
        }
    }

    private void kill() {
        CarEntities.kill(this);
    }

    @Override
    public void a(float sideMot, float f1, float forMot) {
        if (passengers == null || passengers.isEmpty()) {
            super.a(sideMot, f1, forMot);
            status.setSpeed(BigDecimal.ZERO);
            return;
        }

        EntityLiving passenger = (EntityLiving) passengers.get(0);
        if (!(passenger instanceof EntityPlayer)) {
            super.a(sideMot, f1, forMot);
            return;
        }

        status.useFuel(0.05f);

        StringBuilder builder = new StringBuilder();
        builder.append("           ");
        builder.append(ChatColor.GOLD);
        builder.append(ChatColor.BOLD);
        builder.append("燃料計  ");
        builder.append(ChatColor.GREEN);

        float fuelRate = status.getFuel() / model.getMaxFuel();
        int filledRate = Math.round(70 * fuelRate);

        for (int i = 0; i < filledRate; i++) {
            builder.append("ǀ");
        }
        builder.append(ChatColor.RED);
        for (int i = 0; i < 70 - filledRate; i++) {
            builder.append("ǀ");
        }
        builder.append(" ");
        if (Math.round(status.getFuel()) == 0) {
            builder.append(ChatColor.RED);
            builder.append(ChatColor.BOLD);
            builder.append("Empty");
        } else {
            if (fuelRate > 0.7f) {
                builder.append(ChatColor.DARK_GREEN);
            } else if (fuelRate <= 0.7f && fuelRate >= 0.2f) {
                builder.append(ChatColor.GOLD);
            } else {
                builder.append(ChatColor.RED);
            }
            builder.append(ChatColor.BOLD);
            builder.append(Math.round(status.getFuel()));
            builder.append(ChatColor.GRAY);
            builder.append(ChatColor.BOLD);
            builder.append(" / ");
            builder.append(ChatColor.DARK_GREEN);
            builder.append(ChatColor.BOLD);
            builder.append(Math.round(model.getMaxFuel()));
        }
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
        k(calculateSpeed(forInput));
        super.a(sideMot, f1, forMot);
    }

    private float calculateSpeed(float passengerInput) {
        if (status.getFuel() <= 0.0f) {
            return BigDecimal.ZERO.floatValue();
        }

        BigDecimal acceleration = new BigDecimal("0.0085");
        if (passengerInput == 0.0f) {
            if (status.getSpeed().compareTo(BigDecimal.ZERO) > 0) {
                status.setSpeed(status.getSpeed().subtract(acceleration));
            }
        } else if (passengerInput < 0.0f) {
            status.setSpeed(status.getSpeed().subtract(acceleration.add(new BigDecimal("0.010"))));
        }

        Speed maxSpeed;
        if (model.getMaxSpeed() > Speed.values().length) {
            maxSpeed = Speed.NORMAL;
        } else {
            maxSpeed = Speed.values()[model.getMaxSpeed() - 1];
        }
        if (status.getSpeed().floatValue() > maxSpeed.getMax()) {
            return status.getSpeed().floatValue();
        }

        if (passengerInput > 0.0f) {
            status.setSpeed(status.getSpeed().add(acceleration));
        }
        if (status.getSpeed().compareTo(BigDecimal.ZERO) < 0) {
            status.setSpeed(status.getSpeed().multiply(new BigDecimal("0.85")));
        }
        return status.getSpeed().floatValue();
    }

    public CarModel getModel() {
        return model;
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
