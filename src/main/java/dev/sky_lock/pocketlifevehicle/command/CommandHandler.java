package dev.sky_lock.pocketlifevehicle.command;

import dev.sky_lock.pocketlifevehicle.PLVehicle;
import dev.sky_lock.pocketlifevehicle.Permission;
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model;
import dev.sky_lock.pocketlifevehicle.vehicle.model.ModelList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author sky_lock
 */

public class CommandHandler implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        ICommand cmd = new HelpCommand();

        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "give":
                    cmd = new GiveCommand();
                    break;
                case "spawn":
                    cmd = new SpawnCommand();
                    break;
                case "towaway":
                    cmd = new TowawayCommand();
                    break;
                case "model":
                    cmd = new ModelCommand();
                    break;
                case "debug":
                    cmd = new DebugCommand();
                    break;
                case "search":
                    cmd = new SearchCommand();
                    break;
                case "allowworld":
                case "aw":
                    cmd = new AllowWorldCommand();
                    break;
                case "reload":
                    cmd = new ReloadCommand();
                    break;
            }

            if (cmd instanceof IAdminCommand) {
                if (!(Permission.ADMIN_COMMAND.obtained(sender))) {
                    sender.sendMessage(PLVehicle.PREFIX + ChatColor.RED + "このコマンドを実行するための権限がありません");
                    return true;
                }
            }

            if (!(cmd instanceof IConsoleCommand)) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(PLVehicle.PREFIX + ChatColor.RED + "このコマンドはプレイヤーのみ実行できます");
                    return true;
                }
            }
        }
        cmd.execute(sender, command, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> tabCompletes = new ArrayList<>();
        if (args.length < 2) {
            String input = args[0];
            if (Permission.ADMIN_COMMAND.obtained(sender)) {
                tabCompletes.addAll(Stream.of("give", "spawn", "model", "debug", "reload", "allowworld").filter(str -> str.startsWith(input)).collect(Collectors.toList()));
            }
            tabCompletes.addAll(Stream.of("towaway", "search").filter(str -> str.startsWith(input)).collect(Collectors.toList()));
        } else if (args.length  == 2) {
            String input = args[1];
            switch (args[0].toLowerCase()) {
                case "give":
                    tabCompletes.addAll(Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()));
                    break;
                case "spawn":
                    tabCompletes.addAll(Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()));
                    break;
                case "reload":
                    tabCompletes.addAll(Stream.of("from", "to").filter(str -> str.startsWith(input)).collect(Collectors.toList()));
                    break;
                case "search":
                case "towaway":
                    if (Permission.ADMIN_COMMAND.obtained(sender)) {
                        tabCompletes.addAll(Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()));
                    }
            }
        } else if (args.length == 3) {
            String input = args[2];
            if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("spawn")) {
                tabCompletes.addAll(ModelList.unmodified().stream().map(Model::getId).filter(str -> str.startsWith(input)).collect(Collectors.toList()));
            }
        }
        return tabCompletes;
    }
}
