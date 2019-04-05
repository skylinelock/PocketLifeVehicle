package dev.sky_lock.mocar.car;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.packet.ActionBar;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.CraftServer;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Optional;

/**
 * @author sky_lock
 */

public class SeatArmorStand extends EntityArmorStand {
    private Car car;
    private SeatPosition position;
    private SeatPositionControl control;

    public SeatArmorStand(World world) {
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

    void assemble(Car car, SeatPosition position) {
        this.car = car;
        this.position = position;
        this.control = new SeatPositionControl();
        Location center = car.getLocation();
        Location loc = control.calculate(center, position);
        setLocation(loc.getX(), center.getY() - 0.75D, loc.getZ(), center.getYaw(), center.getPitch());
    }

    //降りた時
    @Override
    protected boolean removePassenger(Entity entity) {
        if (!isCarSheet()) {
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

    //足音がなるかどうか
    @Override
    public boolean isSilent() {
        return isCarSheet();
    }

    @Override
    public void movementTick() {
        if (!isDriverSheet()) {
            synchronize();
            return;
        }
        if (!car.getDriver().isPresent()) {
            synchronize();
            return;
        }

        EntityLiving passenger = (EntityLiving) passengers.get(0);
        if (!(passenger instanceof EntityPlayer)) {
            synchronize();
            return;
        }
        car.getMeterPanel().display((Player)((EntityHuman) passenger).getBukkitEntity());

        synchronize();
    }

    boolean isDriverSheet() {
        return position == SeatPosition.ONE_DRIVER || position == SeatPosition.TWO_DRIVER || position == SeatPosition.FOUR_DRIVER;
    }

    private void synchronize() {
        Location loc = control.calculate(car.getLocation(), position);
        this.locX = loc.getX();
        this.locY = car.getLocation().getY() - 0.75D;
        this.locZ = loc.getZ();
        setPosition(locX, locY, locZ);
        this.yaw = car.getLocation().getYaw();
        this.lastYaw = this.yaw;
        this.pitch = car.getLocation().getPitch();
        setYawPitch(yaw, pitch);
    }

    private boolean isCarSheet() {
        return position != null && control != null;
    }

    @Override
    public CraftEntity getBukkitEntity() {
        if (this.bukkitEntity == null || !(this.bukkitEntity instanceof CraftSeat)) {
            this.bukkitEntity = new CraftSeat((CraftServer) Bukkit.getServer(), this);
        }
        return super.getBukkitEntity();
    }

    @Override
    public int getId() {
        return super.getId();
    }

    Optional<Player> getPassenger() {
        if (passengers.isEmpty()) {
            return Optional.empty();
        }
        if (!(passengers.get(0) instanceof EntityHuman)) {
            return Optional.empty();
        }
        return Optional.of((CraftPlayer) passengers.get(0).getBukkitEntity());
    }

    public class CraftSeat extends CraftArmorStand {
        CraftSeat(CraftServer server, EntityArmorStand entity) {
            super(server, entity);
        }
    }
}
