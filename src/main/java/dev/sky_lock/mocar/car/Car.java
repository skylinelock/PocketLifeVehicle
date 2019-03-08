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

import java.util.UUID;

/**
 * @author sky_lock
 */

public class Car {
    private UUID owner;
    private CarModel model;
    private Location location;
    private CarEntity carEntity;
    private boolean isRiding;
    private float speed;
    private float acceleration = 0.0085F;
    private float fuel;

    public void spawn(CarModel model, UUID owner, Location location) {
        this.model = model;
        this.owner = owner;
        this.location = location;
        carEntity = new CarEntity(((CraftWorld) location.getWorld()).getHandle(), this);

        carEntity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        ((CraftWorld) location.getWorld()).getHandle().addEntity(carEntity);
        this.fuel = model.getMaxFuel();
    }

    public void despawn() {
        this.carEntity.killEntity();
    }

    public void tow() {
        despawn();
        carEntity.getBukkitEntity().getWorld().dropItem(carEntity.getBukkitEntity().getLocation(), new ItemStackBuilder(Material.WOOD_HOE, 1).damage(1).build());
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
        if (!carEntity.passengers.isEmpty()) {
            player.sendMessage(MoCar.PREFIX + ChatColor.RED + "他のプレイヤーが乗車中です");
            return;
        }
        carEntity.getBukkitEntity().setPassenger(player);
        player.getLocation().setYaw(carEntity.getBukkitYaw());
        isRiding = true;
    }

    public void dismount(Player player) {
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        if (carEntity.passengers.contains(handle)) {
            carEntity.passengers.remove((handle));
            isRiding = false;
            speed = 0.0f;
        }
    }

    public CarModel getModel() {
        return model;
    }

    public float getFuel() {
        return fuel;
    }

    public void useFuel(float fuelPercentage) {
        if (fuel < 0.0f) {
            return;
        }
        this.fuel -= fuelPercentage;
    }

    void setSpeed(float speed) {
        this.speed = speed;
    }

    boolean isRiding() {
        return isRiding;
    }

    float calculateSpeed(float passengerInput) {
        if (this.fuel <= 0.0F) {
            return 0.0F;
        }
        if (passengerInput == 0.0f) {
            speed -= acceleration;
        } else if (passengerInput < 0.0f) {
            speed -= (acceleration + 0.010f);
        }
        Speed maxSpeed;
        if (model.getMaxSpeed() > Speed.values().length) {
            maxSpeed = Speed.NORMAL;
        } else {
            maxSpeed = Speed.values()[model.getMaxSpeed() - 1];
        }
        if (speed > maxSpeed.getMax()) {
            return speed;
        }
        if (passengerInput > 0.0f) {
            speed += acceleration;
        }
        if (speed <= 0.0f) {
            speed *= 0.25f;// Make backwards slower
        }
        return speed;
    }
}
