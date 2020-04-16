package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.PLVehicle
import dev.sky_lock.pocketlifevehicle.Permission
import dev.sky_lock.pocketlifevehicle.vehicle.Storage
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * @author sky_lock
 */
class CommandHandler : CommandExecutor, TabExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        var cmd: ICommand = HelpCommand()
        if (args.isNotEmpty()) {
            when (args[0].toLowerCase()) {
                "give" -> cmd = GiveCommand()
                "spawn" -> cmd = SpawnCommand()
                "towaway" -> cmd = TowawayCommand()
                "model" -> cmd = ModelCommand()
                "debug" -> cmd = DebugCommand()
                "search" -> cmd = SearchCommand()
                "allowworld", "aw" -> cmd = AllowWorldCommand()
                "reload" -> cmd = ReloadCommand()
            }
            if (cmd is IAdminCommand) {
                if (!Permission.ADMIN_COMMAND.obtained(sender)) {
                    sender.sendMessage(PLVehicle.PREFIX + ChatColor.RED + "このコマンドを実行するための権限がありません")
                    return true
                }
            }
            if (cmd !is IConsoleCommand) {
                if (sender !is Player) {
                    sender.sendMessage(PLVehicle.PREFIX + ChatColor.RED + "このコマンドはプレイヤーのみ実行できます")
                    return true
                }
            }
        }
        cmd.execute(sender, command, args)
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<String>): List<String>? {
        val tabCompletes: MutableList<String> = ArrayList()
        if (args.size < 2) {
            val input = args[0]
            if (Permission.ADMIN_COMMAND.obtained(sender)) {
                tabCompletes.addAll(Stream.of("give", "spawn", "model", "debug", "reload", "allowworld").filter { str: String -> str.startsWith(input) }.collect(Collectors.toList()))
            }
            tabCompletes.addAll(Stream.of("towaway", "search").filter { str: String -> str.startsWith(input) }.collect(Collectors.toList()))
        } else if (args.size == 2) {
            val input = args[1]
            when (args[0].toLowerCase()) {
                "give" -> tabCompletes.addAll(Bukkit.getOnlinePlayers().stream().map { obj: HumanEntity -> obj.name }.collect(Collectors.toList()))
                "spawn" -> tabCompletes.addAll(Bukkit.getOnlinePlayers().stream().map { obj: HumanEntity -> obj.name }.collect(Collectors.toList()))
                "reload" -> tabCompletes.addAll(Stream.of("from", "to").filter { str: String -> str.startsWith(input) }.collect(Collectors.toList()))
                "search", "towaway" -> if (Permission.ADMIN_COMMAND.obtained(sender)) {
                    tabCompletes.addAll(Bukkit.getOnlinePlayers().stream().map { obj: HumanEntity -> obj.name }.collect(Collectors.toList()))
                }
            }
        } else if (args.size == 3) {
            val input = args[2]
            if (args[0].equals("give", ignoreCase = true) || args[0].equals("spawn", ignoreCase = true)) {
                Storage.MODEL.forEach { model: Model ->
                    val id = model.id
                    if (id.startsWith(input)) {
                        tabCompletes.add(id)
                    }
                }
            }
        }
        return tabCompletes
    }
}