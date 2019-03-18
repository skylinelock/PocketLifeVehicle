package dev.sky_lock.mocar.commands;

import dev.sky_lock.mocar.gui.EditModelData;
import dev.sky_lock.mocar.gui.EditSessions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class DebugCommand implements ICommand, IAdminCommand {

    @Override
    public void execute(CommandSender sender, Command cmd, String[] args) {
        EditModelData data = EditSessions.get(((Player) sender).getUniqueId());
        if (data == null) {
            sender.sendMessage("hogehoge");
            return;
        }
        sender.sendMessage(data.getId() + " : " + data.getName());
    }
}
