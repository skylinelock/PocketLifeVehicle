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

public class ListModelCommand implements ICommand {

    @Override
    public void execute(CommandSender sender, Command cmd, String[] args) {
        Player player = (Player) sender;
        if (ModelList.unmodified().isEmpty()) {
            player.sendMessage(MoCar.PREFIX + ChatColor.RED + "Failed : Could not find any models");
            return;
        }
        ModelList.unmodified().forEach(model -> {
            player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "-------------------------------------------");
            player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "ID             : " + model.getId());
            player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "NAME          : " + model.getName());
            player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "LORE          : " + model.getLores());
            player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "SPEED        : " + model.getMaxSpeed());
            player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "MAXFUEL     : " + model.getMaxFuel());
            player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "DISTANCE/L : " + model.getDistancePerLiter());
            player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "-------------------------------------------");
        });
    }
}
