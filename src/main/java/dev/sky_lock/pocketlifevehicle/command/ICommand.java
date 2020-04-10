package dev.sky_lock.pocketlifevehicle.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author sky_lock
 */

public interface ICommand {

    void execute(CommandSender sender, Command cmd, String[] args);
}
