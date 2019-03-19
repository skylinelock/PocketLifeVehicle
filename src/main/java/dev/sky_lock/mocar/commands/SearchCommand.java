package dev.sky_lock.mocar.commands;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.CarArmorStand;
import dev.sky_lock.mocar.car.CarEntities;
import dev.sky_lock.mocar.util.MojangUtil;
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
        if (!player.hasPermission("mocar.commands.admin.use") || args.length < 2) {
            Location location = getCarLocation(player.getUniqueId());
            if (location == null) {
                player.sendMessage(MoCar.PREFIX + ChatColor.RED + "車を所有していません");
            } else {
                sendLocation(player, location);
            }
            return;
        }

        String name = args[1];
        UUID targetUUID = MojangUtil.getUUID(name);
        Location location = getCarLocation(targetUUID);
        if (location == null) {
            player.sendMessage(MoCar.PREFIX + ChatColor.RED + "指定したプレイヤーは車を所有していません");
        } else {
            sendLocation(player, location);
        }
    }

    private Location getCarLocation(UUID target) {
        CarArmorStand car = CarEntities.get(target);
        if (car == null) {
            return null;
        }
        return car.getLocation();
    }

    private void sendLocation(Player player, Location loc) {
        player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "-------------------------------------------");
        player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "World : " + loc.getWorld().getName());
        player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "X : " + loc.getBlockX());
        player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "Y : " + loc.getBlockY());
        player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "Z : " + loc.getBlockZ());
        player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "-------------------------------------------");
    }
}
