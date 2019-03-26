package dev.sky_lock.mocar.command;

import dev.sky_lock.mocar.gui.EditCarModelMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class EditCommand implements ICommand, IAdminCommand {

    @Override
    public void execute(CommandSender sender, Command cmd, String[] args) {
       Player player = (Player) sender;
       new EditCarModelMenu(player).open(player);
    }
}
