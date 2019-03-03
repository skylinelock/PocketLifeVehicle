package dev.sky_lock.mocar.car;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author sky_lock
 */

public class CarEntity {
    private UUID owner;
    private CarModel model;
    private Location location;
    private int currentSpeed;
    private ArmorStand as;

    public void spawn(UUID owner, Location location) {
        this.owner = owner;
        as = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        as.setVisible(true);
        as.setGravity(false);
    }

    public UUID getOwner() {
        return owner;
    }

    public void ride(Player player) {
        if (!player.getUniqueId().equals(owner)) {
            player.sendMessage(ChatColor.RED + "Failed : You are not owner of that vehicle");
            return;
        }
        as.addPassenger(player);
    }

    public void dismount(Player player) {
        if (as.getPassengers().contains(player)) {
            as.removePassenger(player);
        }
    }
}
