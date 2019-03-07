package dev.sky_lock.mocar.car;

import dev.sky_lock.mocar.MoCar;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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

    public void spawn(UUID owner, Location location) {
        this.owner = owner;
        this.location = location;
        carEntity = new CarEntity(((CraftWorld) location.getWorld()).getHandle());
        carEntity.setCar(this);

        carEntity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        ((CraftWorld) location.getWorld()).getHandle().addEntity(carEntity);
    }

    public void despawn() {
        this.carEntity.killEntity();
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
            player.sendMessage(MoCar.PREFIX + ChatColor.RED + "Failed : You are not owner of that vehicle");
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

    void setSpeed(float speed) {
        this.speed = speed;
    }

    boolean isRiding() {
        return isRiding;
    }

    float calculateSpeed(float passengerInput) {
        if (passengerInput == 0.0f) {
            speed -= acceleration;
        } else if (passengerInput < 0.0f) {
            speed -= (acceleration + 0.010f);
        }
        if (speed > 0.50f) {
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
