package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.extension.chat.sendVehiclePrefixedMessage
import dev.sky_lock.pocketlifevehicle.vehicle.ModelRegistry
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

/**
 * @author sky_lock
 */
class ReloadCommand : ICommand, IAdminCommand, IConsoleCommand {
    override fun execute(sender: CommandSender, cmd: Command, args: Array<String>) {
        if (args.size < 2) {
            sender.sendVehiclePrefixedMessage(ChatColor.RED + "引数が足りません")
            return
        }
        val flag = args[1]
        when {
            flag.equals("from", ignoreCase = true) -> {
                ModelRegistry.reloadConfig()
                VehiclePlugin.instance.pluginConfiguration.load()
                sender.sendVehiclePrefixedMessage(ChatColor.GREEN + "ディスクからデータを読み込みました")
            }
            flag.equals("to", ignoreCase = true) -> {
                VehiclePlugin.instance.pluginConfiguration.save()
                ModelRegistry.saveToFile()
                sender.sendVehiclePrefixedMessage(ChatColor.GREEN + "ディスクへデータを保存しました")
            }
            else -> {
                sender.sendVehiclePrefixedMessage("/vehicle reload [from/to]")
            }
        }
    }
}