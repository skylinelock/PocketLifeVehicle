package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.PLVehicle
import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.extension.chat.sendPrefixedPluginMessage
import dev.sky_lock.pocketlifevehicle.vehicle.Storage
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

/**
 * @author sky_lock
 */
class ReloadCommand : ICommand, IAdminCommand, IConsoleCommand {
    override fun execute(sender: CommandSender, cmd: Command, args: Array<String>) {
        if (args.size < 2) {
            sender.sendPrefixedPluginMessage(ChatColor.RED + "引数が足りません")
            return
        }
        val flag = args[1]
        when {
            flag.equals("from", ignoreCase = true) -> {
                Storage.MODEL.reloadConfig()
                PLVehicle.instance.pluginConfiguration.reload()
                sender.sendPrefixedPluginMessage(ChatColor.GREEN + "ディスクからデータを読み込みました")
            }
            flag.equals("to", ignoreCase = true) -> {
                PLVehicle.instance.pluginConfiguration.save()
                Storage.MODEL.saveToFile()
                sender.sendPrefixedPluginMessage(ChatColor.GREEN + "ディスクへデータを保存しました")
            }
            else -> {
                sender.sendPrefixedPluginMessage(ChatColor.RED + "/vehicle reload [from/to]")
            }
        }
    }
}