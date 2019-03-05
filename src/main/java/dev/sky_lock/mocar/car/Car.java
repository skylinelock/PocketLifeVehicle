package dev.sky_lock.mocar.car;

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
    private int currentSpeed;
    private CarEntity carEntity;

    public void spawn(UUID owner, Location location) {
        this.owner = owner;
        this.location = location;
        carEntity = new CarEntity(((CraftWorld) location.getWorld()).getHandle(), this);

        carEntity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        ((CraftWorld) location.getWorld()).getHandle().addEntity(carEntity);
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
            player.sendMessage(ChatColor.RED + "Failed : You are not owner of that vehicle");
            return;
        }
        //carEntity.passengers.add(((CraftPlayer) player).getHandle());
        carEntity.getBukkitEntity().setPassenger(player);
    }

    public void dismount(Player player) {
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        if (carEntity.passengers.contains(handle)) {
            carEntity.passengers.remove((handle));
        }
    }
}
