package dev.sky_lock.mocar.commands;

import dev.sky_lock.mocar.MoCar;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author sky_lock
 */

public class DebugCommand implements ICommand {

    @Override
    public void execute(CommandSender sender, Command cmd, String[] args) {
        sender.sendMessage(MoCar.PREFIX + ChatColor.DARK_GRAY + "Debug");
    }
}
