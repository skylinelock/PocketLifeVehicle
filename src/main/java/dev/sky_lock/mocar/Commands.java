package dev.sky_lock.mocar;

import dev.sky_lock.mocar.car.CarModel;
import dev.sky_lock.mocar.car.Cars;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author sky_lock
 */

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("プレイヤーから実行してください");
            return true;
        }
        Player player = (Player) sender;
        Cars module = MoCar.getInstance().getCarModule();
        if (args.length < 1) {
            module.getCarModels().forEach(model -> {
                player.sendMessage("NAME : " + model.getName());
                model.getLores().forEach(lore -> player.sendMessage("LORE : " + lore));
                player.sendMessage("DISTANCE : " + model.getDistancePerLiter() + "");
                player.sendMessage("MAX_FUEL : " + model.getMaxFuel());
            });
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "search":
                break;
            case "toaway":

        }

        if (!player.hasPermission("mocar.admin.command")) {
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "give":

                break;
            case "addmodel":
                if (args.length < 6) {
                    player.sendMessage(ChatColor.RED + "Failed : Not Enough Arguments");
                    return true;
                }
                String name = args[1];
                String lore = args[2];
                int maxfuel = Integer.valueOf(args[3]);
                int distance = Integer.valueOf(args[4]);
                int speed = Integer.valueOf(args[5]);
                CarModel newModel = new CarModel(name, Arrays.asList(lore), maxfuel, distance, speed);
                module.addModel(newModel);
                player.sendMessage(ChatColor.GREEN + "Success : Add a new car model");
                return true;
            case "removemodel":
                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "Failed : Not Enough Arguments");
                    return true;
                }
                module.removeModel(args[1]);
                player.sendMessage(ChatColor.GREEN + "Success : Remove a car model");
                return true;
            case "list":
                if (module.getCarModels().isEmpty()) {
                    player.sendMessage(ChatColor.RED + "Failed : Could not find any models");
                    return true;
                }
                module.getCarModels().forEach(model -> {
                    player.sendMessage(ChatColor.GREEN + "NAME       : " + model.getName());
                    player.sendMessage(ChatColor.GREEN + "LORE       : " + model.getLores());
                    player.sendMessage(ChatColor.GREEN + "SPEED      : " + model.getSpeed());
                    player.sendMessage(ChatColor.GREEN + "MAXFUEL    : " + model.getMaxFuel());
                    player.sendMessage(ChatColor.GREEN + "DISTANCE/L : " + model.getDistancePerLiter());
                });
                return true;
            case "reload":
                module.reloadConfig();
                player.sendMessage(ChatColor.GREEN + "Success : Reloaded all modules");
                return true;
            case "debug":
                module.loadModules();
                player.sendMessage(ChatColor.GREEN + "Success : Debug");
                return true;
        }
        player.sendMessage(ChatColor.RED + "Command failed");
        return true;
    }
}
