package dev.sky_lock.mocar.commands;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.CarItem;
import dev.sky_lock.mocar.car.CarModel;
import dev.sky_lock.mocar.car.ModelList;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
        if (args.length < 7) {
            player.sendMessage(MoCar.PREFIX + ChatColor.RED + "Failed : Not Enough Arguments");
            return;
        }
        String id = args[1];
        double damage = Double.valueOf(args[2]);
        String name = args[3];
        String lore = args[4];
        float maxFuel = Float.valueOf(args[5]);
        int speed = Integer.valueOf(args[6]);
        CarModel newModel = new CarModel(id, new CarItem(Material.WOOD_HOE, damage), name, Collections.singletonList(lore), maxFuel, speed);
        boolean success = ModelList.add(newModel);
        if (success) {
            player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "Success : Add a new car model");
        } else {
            player.sendMessage(MoCar.PREFIX + ChatColor.RED + "Failed : Could not add a new car model");
        }
    }
}
