
package dev.sky_lock.mocar.car;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.gui.CarUtilMenu;
import dev.sky_lock.mocar.packet.ActionBar;
import dev.sky_lock.mocar.task.BurnExplosionTask;
import dev.sky_lock.mocar.task.SubmergedMessageTask;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.CraftServer;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * @author sky_lock
 */

public class CarArmorStand extends EntityArmorStand {

    private CarUtilMenu menu;
    private CarModel model;
    private CarStatus status;

    private Engine engine;
    private Steering steering;
    private MeterPanel meterPanel;
    private boolean beginExplode = false;

    public CarArmorStand(World world) {
        super(world);

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("NoBasePlate", true);
        nbt.setBoolean("Invulnerable", true);
        nbt.setBoolean("PersistenceRequired", true);
        nbt.setBoolean("NoGravity", false);
        nbt.setBoolean("Invisible", true);
        nbt.setBoolean("Marker", false);
        this.a(nbt);
        this.getBukkitEntity().setMetadata("mocar-as", new FixedMetadataValue(MoCar.getInstance(), null));
    }

    void assemble(CarModel model) {
        this.model = model;
        this.setSlot(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(model.getItem().getStack(model.getName())));
        this.status = new CarStatus();
        this.engine = new Engine(status, model);
        this.steering = new Steering(status);
        this.meterPanel = new MeterPanel(status, model, engine);
        //乗れるブロックの高さ
        this.Q = 1.0F;
        setSize(2.5F, 2.5F);
    }

    public void openUtilMenu(Player player) {
        if (this.menu == null) {
            this.menu = new CarUtilMenu(player, this);
        }
        this.menu.open(player);
    }

    public Location getLocation() {
        return getBukkitEntity().getLocation();
    }

    public CarStatus getStatus() {
        return status;
    }

    public boolean refuel(float fuel) {
        return engine.refuel(fuel);
    }

    //降りた時
    @Override
    protected boolean removePassenger(Entity entity) {
        if (!isCarArmorStand()) {
            super.removePassenger(entity);
            return true;
        }
        super.removePassenger(entity);
        if (!(entity.getBukkitEntity() instanceof Player)) {
            return true;
        }
        Player player = (Player) entity.getBukkitEntity();
        ActionBar.sendPacket(player, "");
        return true;
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
        new SubmergedMessageTask().run(this);
    }

    @Override
    protected void burn(float i) {
        if (!isCarArmorStand()) {
            super.burn(i);
            return;
        }
        super.burn(i);
        if (!beginExplode) {
            new BurnExplosionTask().run(this);
            this.beginExplode = true;
        }
    }

    @Override
    public void a(float sideMot, float f1, float forMot) {
        if (!isCarArmorStand()) {
            super.a(sideMot, f1, forMot);
            return;
        }
        if (passengers == null || passengers.isEmpty()) {
            status.getSpeed().zero();
            super.a(sideMot, f1, forMot);
            return;
        }

        EntityLiving passenger = (EntityLiving) passengers.get(0);
        if (!(passenger instanceof EntityPlayer)) {
            super.a(sideMot, f1, forMot);
            return;
        }

        float sideInput = passenger.bh;
        float forInput = passenger.bj;

        sideMot = 0.0f;
        forMot = 3.0f;

        this.fallDistance = 0.0F;

        if (sideInput < 0.0F) {
            steering.right();
        } else if (sideInput > 0.0F) {
            steering.left();
        }

        this.yaw = status.getYaw();
        this.lastYaw = this.yaw;
        this.pitch = passenger.pitch * 0.5F;
        setYawPitch(this.yaw, this.pitch);
        this.aQ = this.yaw;
        this.aS = this.aQ;

        this.aU = this.cK() * 0.1f;

        this.aI = this.aJ;
        double d0 = this.locX - this.lastX;
        double d1 = this.locZ - this.lastZ;
        double f4 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0f;

        if (f4 > 1.0f) {
            f4 = 1.0f;
        }

        this.aJ += (f4 - this.aJ) * 0.4f;
        this.aK += this.aJ;
        this.o(engine.speedPerTick(forInput));
        super.a(sideMot, f1, forMot);

        engine.consumeFuel(sideInput);
        meterPanel.display((Player)((EntityHuman) passenger).getBukkitEntity());

    }

    public CarModel getModel() {
        return model;
    }

    @Override
    public CraftEntity getBukkitEntity() {
        if (!isCarArmorStand()) {
            if (this.bukkitEntity == null || !(this.bukkitEntity instanceof CraftArmorStand)) {
                this.bukkitEntity = new CraftArmorStand((CraftServer) Bukkit.getServer(), this);
            }
            return super.getBukkitEntity();
        }

        if (this.bukkitEntity == null || !(this.bukkitEntity instanceof CraftCar)) {
            this.bukkitEntity = new CraftCar((CraftServer) Bukkit.getServer(), this);
        }
        return super.getBukkitEntity();
    }

    @Override
    public int getId() {
        return super.getId();
    }

    public boolean isCarArmorStand() {
        return this.model != null && status != null;
    }

}