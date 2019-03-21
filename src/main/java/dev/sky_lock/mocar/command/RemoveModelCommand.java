package dev.sky_lock.mocar.command;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.ModelList;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class RemoveModelCommand implements ICommand, IAdminCommand {

    @Override
    public void execute(CommandSender sender, Command cmd, String[] args) {
        Player player = (Player) sender;
        if (args.length < 2) {
            player.sendMessage(MoCar.PREFIX + ChatColor.RED + "引数が足りません");
            return;
        }
        boolean success = ModelList.remove(args[1]);
        if (success) {
            player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "指定した車種を削除しました");
        } else {
            player.sendMessage(MoCar.PREFIX + ChatColor.RED + "車種が存在しません");
        }
    }
}
