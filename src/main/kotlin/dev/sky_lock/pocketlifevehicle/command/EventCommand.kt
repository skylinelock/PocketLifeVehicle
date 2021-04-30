package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.extension.chat.sendVehiclePrefixedMessage
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleManager
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */

class EventCommand : ICommand, IAdminCommand {

    override fun execute(sender: CommandSender, cmd: Command, args: Array<String>) {
        val player = sender as Player
        if (args.size < 2) {
            player.sendVehiclePrefixedMessage(ChatColor.RED + "引数が足りません")
            return
        }
        val arg = args[1].toLowerCase()
        when(arg) {
            "clear" -> {
                VehicleManager.removeEventVehicles()
                player.sendVehiclePrefixedMessage(ChatColor.GREEN + "全てのイベント車両を削除しました")
            }
            "unlock" -> {
                VehicleManager.setLockForEventVehicles(false)
                player.sendVehiclePrefixedMessage(ChatColor.GREEN + "全てのイベント車両をアンロックしました")
            }
            "lock" -> {
                VehicleManager.setLockForEventVehicles(true)
                player.sendVehiclePrefixedMessage(ChatColor.GREEN + "全てのイベント車両をロックしました")
            }
        }
    }
}