package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.PLVehicle
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
            sender.sendMessage(PLVehicle.PREFIX + ChatColor.RED + "引数が足りません")
            return
        }
        val flag = args[1]
        when {
            flag.equals("from", ignoreCase = true) -> {
                Storage.MODEL.reloadConfig()
                PLVehicle.getInstance().pluginConfig.reloadFromDisk()
                sender.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "ディスクからデータを読み込みました")
            }
            flag.equals("to", ignoreCase = true) -> {
                PLVehicle.getInstance().pluginConfig.saveToFile()
                Storage.MODEL.saveToFile()
                sender.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "ディスクへデータを保存しました")
            }
            else -> {
                sender.sendMessage(PLVehicle.PREFIX + ChatColor.RED + "/vehicle reload [from/to]")
            }
        }
    }
}