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
                    cmd = new GiveCommand(handler);
                    break;
                case "addmodel":
                    cmd = new AddModelCommand(handler);
                    break;
                case "removemodel":
                    cmd = new RemoveModelCommand(handler);
                    break;
                case "debug":
                    cmd = new DebugCommand();
                    break;
                case "listmodel":
                    cmd = new ListModelCommand(handler);
                    break;
                case "search":
                    cmd = new SearchCommand(handler);
                    break;
                case "reload":
                    handler.reloadConfig();
                    sender.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "Success : Reloaded all modules");
                    return true;
                case "ride":
                    Player player = (Player) sender;
                    handler.getCar(player).ride(player);
                    return true;
                case "dismount":
                    player = (Player) sender;
                    handler.getCar(player).dismount(player);
                    return true;

            }
        }
        cmd.execute(sender, command, args);
        return true;
    }
}
