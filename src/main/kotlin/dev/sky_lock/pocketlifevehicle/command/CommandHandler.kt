package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.Permission
import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.extension.chat.sendVehiclePrefixedMessage
import dev.sky_lock.pocketlifevehicle.vehicle.ModelRegistry
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import java.util.*

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
                "pop" -> cmd = PopCommand()
                "model" -> cmd = ModelCommand()
                "debug" -> cmd = DebugCommand()
                "search" -> cmd = SearchCommand()
                "world" -> cmd = WorldCommand()
                "reload" -> cmd = ReloadCommand()
            }
            if (cmd is IAdminCommand) {
                if (!Permission.ADMIN_COMMAND.obtained(sender)) {
                    sender.sendVehiclePrefixedMessage(ChatColor.RED + "このコマンドを実行するための権限がありません")
                    return true
                }
            }
            if (cmd !is IConsoleCommand) {
                if (sender !is Player) {
                    sender.sendVehiclePrefixedMessage(ChatColor.RED + "このコマンドはプレイヤーのみ実行できます")
                    return true
                }
            }
        }
        cmd.execute(sender, command, args)
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<String>): List<String> {
        val tabCompletes: MutableList<String> = ArrayList()
        if (args.size < 2) {
            val input = args[0]
            if (Permission.ADMIN_COMMAND.obtained(sender)) {
                tabCompletes.addAll(listOf("give", "spawn", "model", "debug", "reload", "world").filter { str -> str.startsWith(input) })
            }
            tabCompletes.addAll(listOf("pop", "search").filter { str -> str.startsWith(input) })
        } else if (args.size == 2 && Permission.ADMIN_COMMAND.obtained(sender)) {
            val input = args[1]
            when (args[0].toLowerCase()) {
                "give" -> tabCompletes.addAll(listPlayerNamesStartsWith(input))
                "spawn" -> tabCompletes.addAll(listPlayerNamesStartsWith(input))
                "reload" -> tabCompletes.addAll(listOf("from", "to").filter { str -> str.startsWith(input) })
                "search", "pop" -> if (Permission.ADMIN_COMMAND.obtained(sender)) {
                    tabCompletes.addAll(listPlayerNamesStartsWith(input))
                }
                "world" -> tabCompletes.addAll(listOf("add", "remove", "list").filter { str -> str.startsWith(input) })
            }
        } else if (args.size == 3 && Permission.ADMIN_COMMAND.obtained(sender)) {
            val input = args[2]
            if (args[0].equals("give", ignoreCase = true) || args[0].equals("spawn", ignoreCase = true)) {
                ModelRegistry.forEach { model: Model ->
                    val id = model.id
                    if (id.startsWith(input)) {
                        tabCompletes.add(id)
                    }
                }
            }
        }
        return tabCompletes
    }

    fun listPlayerNamesStartsWith(input: String): List<String> {
        return Bukkit.getOnlinePlayers().filter { player -> player.name.startsWith(input) }.map { player -> player.name }
    }
}