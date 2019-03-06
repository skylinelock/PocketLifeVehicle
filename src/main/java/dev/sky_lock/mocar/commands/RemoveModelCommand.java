package dev.sky_lock.mocar.commands;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.CarHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class RemoveModelCommand implements ICommand {
    private final CarHandler handler;

    public RemoveModelCommand(CarHandler handler) {
        this.handler = handler;
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MoCar.PREFIX + ChatColor.RED + "このコマンドはプレイヤーのみ実行できます");
            return;
        }
        Player player = (Player) sender;
        if (args.length < 2) {
            player.sendMessage(MoCar.PREFIX + ChatColor.RED + "Failed : Not Enough Arguments");
            return;
        }
        handler.removeModel(args[1]);
        player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "Success : Remove a car model");
    }
}
