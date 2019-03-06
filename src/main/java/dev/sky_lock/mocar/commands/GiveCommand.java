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
        MoCar.getInstance().getCarHandler().spawnAt(player, player.getLocation());
        player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "Success : Spawned a vehicle");
    }
}
