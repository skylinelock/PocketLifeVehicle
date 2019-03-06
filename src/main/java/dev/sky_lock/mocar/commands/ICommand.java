package dev.sky_lock.mocar.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author sky_lock
 */

public interface ICommand {

    void execute(CommandSender sender, Command cmd, String[] args);
}
