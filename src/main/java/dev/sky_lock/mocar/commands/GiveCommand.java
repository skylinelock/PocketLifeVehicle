package dev.sky_lock.mocar.commands;

import dev.sky_lock.mocar.MoCar;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class GiveCommand implements ICommand, IAdminCommand {

    @Override
    public void execute(CommandSender sender, Command cmd, String[] args) {
        Player player = (Player) sender;
        if (args.length < 2) {
            player.sendMessage(MoCar.PREFIX + ChatColor.RED + "引数が足りません");
            return;
        }
        boolean success = MoCar.getInstance().getCarHandler().spawnAt(MoCar.getInstance().getCarHandler().getModel(args[1]), player, player.getLocation());
        if (success) {
            player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "Success : Summoned '" + args[1] + "'");
        } else {
            player.sendMessage(MoCar.PREFIX + ChatColor.RED + "Failed : Could not summon '" + args[1] + "'");
        }
    }
}
