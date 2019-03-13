package dev.sky_lock.mocar.commands;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.CarEntity;
import dev.sky_lock.mocar.car.CarSet;
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
        CarSet handler = MoCar.getInstance().getCarHandler();
        if (handler.getCarEntity(player) == null) {
            player.sendMessage(MoCar.PREFIX + ChatColor.RED + "Failed : You don't have any cars");
            return;
        }
        CarEntity carEntity = handler.getCarEntity(player);
        org.bukkit.Location loc = carEntity.getLocation();
        player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "-------------------------------------------");
        player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "World : " + loc.getWorld().getName());
        player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "X : " + loc.getBlockX());
        player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "Y : " + loc.getBlockY());
        player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "Z : " + loc.getBlockZ());
        player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "-------------------------------------------");
    }
}
