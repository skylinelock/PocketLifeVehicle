package dev.sky_lock.mocar.command;

import dev.sky_lock.mocar.gui.CarModelEditor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class EditCommand implements ICommand, IAdminCommand {

    @Override
    public void execute(CommandSender sender, Command cmd, String[] args) {
        CarModelEditor editor = new CarModelEditor((Player) sender);
        editor.open((Player) sender);
    }
}
