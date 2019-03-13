package dev.sky_lock.mocar.commands;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.CarModel;
import dev.sky_lock.mocar.car.ModelList;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

/**
 * @author sky_lock
 */

public class AddModelCommand implements ICommand, IAdminCommand {

    @Override
    public void execute(CommandSender sender, Command cmd, String[] args) {
        Player player = (Player) sender;
        if (args.length < 6) {
            player.sendMessage(MoCar.PREFIX + ChatColor.RED + "Failed : Not Enough Arguments");
            return;
        }
        String id = args[1];
        String name = args[2];
        String lore = args[3];
        int distance = Integer.valueOf(args[4]);
        float maxFuel = Float.valueOf(args[5]);
        int speed = Integer.valueOf(args[6]);
        CarModel newModel = new CarModel(id, name, Collections.singletonList(lore), distance, maxFuel, speed);
        boolean success = ModelList.add(newModel);
        if (success) {
            player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "Success : Add a new car model");
        } else {
            player.sendMessage(MoCar.PREFIX + ChatColor.RED + "Failed : Could not add a new car model");
        }
    }
}
