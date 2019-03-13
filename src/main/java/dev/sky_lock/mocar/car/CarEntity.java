package dev.sky_lock.mocar.car;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.util.ItemStackBuilder;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author sky_lock
 */

public class CarEntity {
    private static final List<CarEntity> entityCars = new ArrayList<>();

    private final UUID owner;
    private final CarModel model;
    private Location location;
    private CarArmorStand armorStand;
    private boolean isRiding;
    private boolean isRunning;
    private BigDecimal speed;
    private float fuel;

    public CarEntity(UUID owner, CarModel model) {
        this.owner = owner;
        this.model = model;
    }

    public static boolean spawn(UUID owner, CarModel model, Location location) {
        if (owner == null || model == null || location == null) {
            return false;
        }
        CarEntity carEntity = new CarEntity(owner, model);

        CarArmorStand armorStand = new CarArmorStand(((CraftWorld) location.getWorld()).getHandle(), carEntity);
        armorStand.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        carEntity.setEntity(armorStand);

        ((CraftWorld) location.getWorld()).getHandle().addEntity(armorStand);
        carEntity.setFuel(model.getMaxFuel());

        entityCars.stream().filter(car -> car.getOwner().equals(owner)).findFirst().ifPresent(entityCars::remove);
        entityCars.add(carEntity);
        return true;
    }

    public static void kill(UUID owner) {
        entityCars.stream().filter(car -> car.getOwner().equals(owner)).findFirst().ifPresent(CarEntity::kill);
    }

    public static CarEntity get(UUID owner) {
        return entityCars.stream().filter(car -> car.getOwner().equals(owner)).findFirst().orElse(null);
    }

    public void kill() {
        this.armorStand.killEntity();
        entityCars.remove(this);
    }

    public void tow() {
        kill();
        armorStand.getBukkitEntity().getWorld().dropItem(armorStand.getBukkitEntity().getLocation(), new ItemStackBuilder(Material.WOOD_HOE, 1).damage(1).build());
    }

    public void setEntity(CarArmorStand armorStand) {
        this.armorStand = armorStand;
    }

    public void setFuel(float fuel) {
        this.fuel = fuel;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public UUID getOwner() {
        return owner;
    }

    public void ride(Player player) {
        if (!player.getUniqueId().equals(owner)) {
            player.sendMessage(MoCar.PREFIX + ChatColor.RED + "あなたはその車を所有していません");
            return;
        }
        if (!armorStand.passengers.isEmpty()) {
            player.sendMessage(MoCar.PREFIX + ChatColor.RED + "他のプレイヤーが乗車中です");
            return;
        }
        armorStand.getBukkitEntity().setPassenger(player);
        player.getLocation().setYaw(armorStand.getBukkitYaw());
        isRiding = true;
    }

    public void dismount(Player player) {
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        if (armorStand.passengers.contains(handle)) {
            armorStand.passengers.remove((handle));
            isRiding = false;
            speed = BigDecimal.ZERO;
        }
    }

    public CarModel getModel() {
        return model;
    }

    public float getFuel() {
        return fuel;
    }

    public void useFuel(float used) {
        if (speed.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }
        if (fuel < 0.0f) {
            return;
        }
        this.fuel -= used;
    }

    public void setSpeed(BigDecimal speed) {
        this.speed = speed;
    }

    boolean isRiding() {
        return isRiding;
    }

    public float calculateSpeed(float passengerInput) {
        if (this.fuel <= 0.0f) {
            isRunning = false;
            return BigDecimal.ZERO.floatValue();
        }

        BigDecimal acceleration = new BigDecimal("0.0085");
        if (passengerInput == 0.0f) {
            if (speed.compareTo(BigDecimal.ZERO) > 0) {
                speed = speed.subtract(acceleration);
            }
        } else if (passengerInput < 0.0f) {
            speed = speed.subtract(acceleration.add(new BigDecimal("0.010")));
        }

        Speed maxSpeed;
        if (model.getMaxSpeed() > Speed.values().length) {
            maxSpeed = Speed.NORMAL;
        } else {
            maxSpeed = Speed.values()[model.getMaxSpeed() - 1];
        }
        if (speed.floatValue() > maxSpeed.getMax()) {
            return speed.floatValue();
        }

        if (passengerInput > 0.0f) {
            speed = speed.add(acceleration);
            isRunning = true;
        }
        if (speed.compareTo(BigDecimal.ZERO) < 0) {
            speed = speed.multiply(new BigDecimal("0.85"));
        }
        return speed.floatValue();
    }
}
