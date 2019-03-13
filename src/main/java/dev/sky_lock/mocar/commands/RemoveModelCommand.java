package dev.sky_lock.mocar.commands;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.ModelList;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class RemoveModelCommand implements ICommand, IAdminCommand {

    @Override
    public void execute(CommandSender sender, Command cmd, String[] args) {
        Player player = (Player) sender;
        if (args.length < 2) {
            player.sendMessage(MoCar.PREFIX + ChatColor.RED + "Failed : Not Enough Arguments");
            return;
        }
        ModelList.remove(args[1]);
        player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "Success : Removed a car model");
    }
}
