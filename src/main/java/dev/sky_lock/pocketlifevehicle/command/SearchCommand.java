package dev.sky_lock.pocketlifevehicle.command;

import dev.sky_lock.pocketlifevehicle.PLVehicle;
import dev.sky_lock.pocketlifevehicle.Permission;
import dev.sky_lock.pocketlifevehicle.vehicle.Car;
import dev.sky_lock.pocketlifevehicle.vehicle.CarEntities;
import dev.sky_lock.pocketlifevehicle.util.Profiles;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author sky_lock
 */

public class SearchCommand implements ICommand {

    @Override
    public void execute(CommandSender sender, Command cmd, String[] args) {
        Player player = (Player) sender;
        if (!Permission.ADMIN_COMMAND.obtained(player) || args.length < 2) {
            Location location = getCarLocation(player.getUniqueId());
            if (location == null) {
                player.sendMessage(PLVehicle.PREFIX + ChatColor.RED + "車を所有していません");
            } else {
                sendLocation(player, location);
            }
            return;
        }

        String name = args[1];
        UUID targetUUID = Profiles.fetchUUID(name);
        Location location = getCarLocation(targetUUID);
        if (location == null) {
            player.sendMessage(PLVehicle.PREFIX + ChatColor.RED + "指定したプレイヤーは車を所有していません");
        } else {
            sendLocation(player, location);
        }
    }

    private Location getCarLocation(UUID target) {
        Car car = CarEntities.of(target);
        if (car == null) {
            return null;
        }
        return car.getLocation();
    }

    private void sendLocation(Player player, Location loc) {
        player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "-------------------------------------------");
        player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "World : " + loc.getWorld().getName());
        player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "X : " + loc.getBlockX());
        player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "Y : " + loc.getBlockY());
        player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "Z : " + loc.getBlockZ());
        player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "-------------------------------------------");
    }
}
