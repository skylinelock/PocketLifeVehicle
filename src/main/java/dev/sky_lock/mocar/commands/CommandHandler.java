package dev.sky_lock.mocar.commands;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.CarHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class CommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        ICommand cmd = new HelpCommand();

        CarHandler handler = MoCar.getInstance().getCarHandler();

        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "help":
                    break;
                case "give":
                    cmd = new GiveCommand();
                    break;
                case "addmodel":
                    cmd = new AddModelCommand();
                    break;
                case "removemodel":
                    cmd = new RemoveModelCommand();
                    break;
                case "debug":
                    cmd = new DebugCommand();
                    break;
                case "listmodel":
                    cmd = new ListModelCommand();
                    break;
                case "search":
                    cmd = new SearchCommand();
                    break;
                case "reload":
                    cmd = new ReloadCommand();
                    break;
                case "ride":
                    Player player = (Player) sender;
                    handler.getCar(player).ride(player);
                    return true;
                case "dismount":
                    player = (Player) sender;
                    handler.getCar(player).dismount(player);
                    return true;
            }

            if (cmd instanceof IAdminCommand) {
                if (!(sender.hasPermission("mocar.admin.command"))) {
                    sender.sendMessage(MoCar.PREFIX + ChatColor.RED + "このコマンドを実行するための権限がありません");
                    return true;
                }
            }

            if (!(cmd instanceof IConsoleCommand)) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(MoCar.PREFIX + ChatColor.RED + "このコマンドはプレイヤーのみ実行できます");
                    return true;
                }
            }
        }
        cmd.execute(sender, command, args);
        return true;
    }
}