package dev.sky_lock.mocar;

import dev.sky_lock.mocar.car.Car;
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
            player.sendMessage("Success : Show help");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "search":
                if (module.getCar(player) == null) {
                    player.sendMessage(ChatColor.RED + "Failed : You don't have any cars");
                    return true;
                }
                Car car = module.getCar(player);
                org.bukkit.Location loc = car.getLocation();
                player.sendMessage(ChatColor.GREEN + "-------------------------------------------");
                player.sendMessage(ChatColor.GREEN + "World : " + loc.getWorld().getName());
                player.sendMessage(ChatColor.GREEN + "X : " + loc.getBlockX());
                player.sendMessage(ChatColor.GREEN + "Y : " + loc.getBlockY());
                player.sendMessage(ChatColor.GREEN + "Z : " + loc.getBlockZ());
                player.sendMessage(ChatColor.GREEN + "-------------------------------------------");
                return true;
            case "toaway":

        }

        if (!player.hasPermission("mocar.admin.command")) {
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "give":
                module.spawnAt(player, player.getLocation());
                player.sendMessage(ChatColor.GREEN + "Success : Spawned a vehicle");
                return true;
            case "addmodel":
                if (args.length < 6) {
                    player.sendMessage(ChatColor.RED + "Failed : Not Enough Arguments");
                    return true;
                }
                String id = args[1];
                String name = args[2];
                String lore = args[3];
                int maxfuel = Integer.valueOf(args[4]);
                int distance = Integer.valueOf(args[5]);
                int speed = Integer.valueOf(args[6]);
                CarModel newModel = new CarModel(id, name, Arrays.asList(lore), maxfuel, distance, speed);
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
                    player.sendMessage(ChatColor.GREEN + "-------------------------------------------");
                    player.sendMessage(ChatColor.GREEN + "ID         : " + model.getId());
                    player.sendMessage(ChatColor.GREEN + "NAME       : " + model.getName());
                    player.sendMessage(ChatColor.GREEN + "LORE       : " + model.getLores());
                    player.sendMessage(ChatColor.GREEN + "SPEED      : " + model.getSpeed());
                    player.sendMessage(ChatColor.GREEN + "MAXFUEL    : " + model.getMaxFuel());
                    player.sendMessage(ChatColor.GREEN + "DISTANCE/L : " + model.getDistancePerLiter());
                    player.sendMessage(ChatColor.GREEN + "-------------------------------------------");
                });
                return true;
            case "reload":
                module.reloadConfig();
                player.sendMessage(ChatColor.GREEN + "Success : Reloaded all modules");
                return true;
            case "ride":
                module.getCar(player).ride(player);
                return true;
            case "dismount":
                module.getCar(player).dismount(player);
                return true;
            case "debug":
                break;
        }
        player.sendMessage(ChatColor.RED + "Command failed");
        return true;
    }
}
