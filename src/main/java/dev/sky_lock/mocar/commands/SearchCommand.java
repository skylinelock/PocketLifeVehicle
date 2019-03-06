package dev.sky_lock.mocar.commands;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.Car;
import dev.sky_lock.mocar.car.CarHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class SearchCommand implements ICommand {

    @Override
    public void execute(CommandSender sender, Command cmd, String[] args) {
        Player player = (Player) sender;
        CarHandler handler = MoCar.getInstance().getCarHandler();
        if (handler.getCar(player) == null) {
            player.sendMessage(MoCar.PREFIX + ChatColor.RED + "Failed : You don't have any cars");
            return;
        }
        Car car = handler.getCar(player);
        org.bukkit.Location loc = car.getLocation();
        player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "-------------------------------------------");
        player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "World : " + loc.getWorld().getName());
        player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "X : " + loc.getBlockX());
        player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "Y : " + loc.getBlockY());
        player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "Z : " + loc.getBlockZ());
        player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "-------------------------------------------");
    }
}
