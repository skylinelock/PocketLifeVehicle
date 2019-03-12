package dev.sky_lock.mocar.commands;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.CarModel;
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
        float maxfuel = Float.valueOf(args[5]);
        int speed = Integer.valueOf(args[6]);
        CarModel newModel = new CarModel(id, name, Collections.singletonList(lore), distance, maxfuel, speed);
        MoCar.getInstance().getCarHandler().addModel(newModel);
        player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "Success : Add a new car model");
    }
}
