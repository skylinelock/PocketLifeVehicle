package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.Permission
import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.extension.chat.sendPrefixedPluginMessage
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleManager
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */
class PopCommand : ICommand {
    override fun execute(sender: CommandSender, cmd: Command, args: Array<String>) {
        val player = sender as Player
        if (args.size < 2 || !Permission.ADMIN_COMMAND.obtained(sender)) {
            if (VehicleManager.hasVehicle(player.uniqueId)) {
                VehicleManager.pop(player.uniqueId)
                player.sendPrefixedPluginMessage(ChatColor.GREEN + "乗り物をアイテム化しました")
            } else {
                player.sendPrefixedPluginMessage(ChatColor.RED + "乗り物をアイテム化できませんでした")
            }
            return
        }
        val name = args[1]
        val targetUUID = Bukkit.getPlayerUniqueId(name)
        if (targetUUID == null) {
            player.sendPrefixedPluginMessage(ChatColor.GREEN + "プレイヤーが見つかりませんでした")
            return
        }
        if (VehicleManager.hasVehicle(targetUUID)) {
            VehicleManager.pop(targetUUID)
            player.sendPrefixedPluginMessage(ChatColor.GREEN + name + " の乗り物をアイテム化しました")
        } else {
            player.sendPrefixedPluginMessage(ChatColor.RED + name + " の乗り物をアイテム化できませんでした")
        }
    }
}