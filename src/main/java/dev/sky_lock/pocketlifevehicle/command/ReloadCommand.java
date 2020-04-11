package dev.sky_lock.pocketlifevehicle.command;

import dev.sky_lock.pocketlifevehicle.PLVehicle;
import dev.sky_lock.pocketlifevehicle.vehicle.model.ModelList;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author sky_lock
 */

public class ReloadCommand implements ICommand, IAdminCommand, IConsoleCommand {

    @Override
    public void execute(CommandSender sender, Command cmd, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(PLVehicle.PREFIX + ChatColor.RED + "引数が足りません");
            return;
        }
        String flag = args[1];

        if (flag.equalsIgnoreCase("from")) {
            ModelList.reloadConfig();
            PLVehicle.getInstance().getPluginConfig().reloadFromDisk();
            sender.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "ディスクからデータを読み込みました");
        } else if (flag.equalsIgnoreCase("to")) {
            PLVehicle.getInstance().getPluginConfig().saveToFile();
            ModelList.saveConfig();
            sender.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "ディスクへデータを保存しました");
        } else {
            sender.sendMessage(PLVehicle.PREFIX + "/vehicle reload [from/to]");
        }
    }
}
