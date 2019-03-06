package dev.sky_lock.mocar.commands;

import dev.sky_lock.mocar.MoCar;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author sky_lock
 */

public class ReloadCommand implements ICommand, IAdminCommand, IConsoleCommand {

    @Override
    public void execute(CommandSender sender, Command cmd, String[] args) {
        MoCar.getInstance().getCarHandler().reloadConfig();
        sender.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "Success : Reloaded all modules");
    }
}
