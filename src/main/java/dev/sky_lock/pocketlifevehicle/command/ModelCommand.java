package dev.sky_lock.pocketlifevehicle.command;

import dev.sky_lock.pocketlifevehicle.gui.ModelMenuIndex;
import dev.sky_lock.pocketlifevehicle.gui.ModelSettingMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class ModelCommand implements ICommand, IAdminCommand {

    @Override
    public void execute(CommandSender sender, Command cmd, String[] args) {
       Player player = (Player) sender;
       new ModelSettingMenu(player).open(player, ModelMenuIndex.MAIN_MENU.ordinal());
    }
}
