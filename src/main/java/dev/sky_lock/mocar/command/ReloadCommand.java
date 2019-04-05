package dev.sky_lock.mocar.command;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.ModelList;
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
            sender.sendMessage(MoCar.PREFIX + ChatColor.RED + "引数が足りません");
            return;
        }
        String flag = args[1];

        if (flag.equalsIgnoreCase("from")) {
            ModelList.reloadConfig();
            MoCar.getInstance().getPluginConfig().reloadFromDisk();
            sender.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "ディスクからデータを読み込みました");
        } else if (flag.equalsIgnoreCase("to")) {
            MoCar.getInstance().getPluginConfig().saveToFile();
            ModelList.saveConfig();
            sender.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "ディスクへデータを保存しました");
        } else {
            sender.sendMessage(MoCar.PREFIX + "/mocar reload [from/to]");
        }
    }
}
