package dev.sky_lock.mocar.commands;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.CarHandler;
import dev.sky_lock.mocar.car.CarModel;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author sky_lock
 */

public class AddModelCommand implements ICommand{
    private final CarHandler handler;

    public AddModelCommand(CarHandler handler) {
        this.handler = handler;
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MoCar.PREFIX + ChatColor.RED + "このコマンドはプレイヤーのみ実行できます");
            return;
        }
        Player player = (Player) sender;
        if (args.length < 6) {
            player.sendMessage(MoCar.PREFIX + ChatColor.RED + "Failed : Not Enough Arguments");
            return;
        }
        String id = args[1];
        String name = args[2];
        String lore = args[3];
        int maxfuel = Integer.valueOf(args[4]);
        int distance = Integer.valueOf(args[5]);
        int speed = Integer.valueOf(args[6]);
        CarModel newModel = new CarModel(id, name, Arrays.asList(lore), maxfuel, distance, speed);
        handler.addModel(newModel);
        player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "Success : Add a new car model");
    }
}
