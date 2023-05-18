package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.Permission
import dev.sky_lock.pocketlifevehicle.text.ext.sendVehiclePrefixedErrorMessage
import dev.sky_lock.pocketlifevehicle.text.ext.sendVehiclePrefixedSuccessMessage
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleManager
import org.bukkit.Bukkit
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
                player.sendVehiclePrefixedSuccessMessage( "乗り物をアイテム化しました")
            } else {
                player.sendVehiclePrefixedErrorMessage( "乗り物をアイテム化できませんでした")
            }
            return
        }
        val name = args[1]
        val targetUUID = Bukkit.getPlayerUniqueId(name)
        if (targetUUID == null) {
            player.sendVehiclePrefixedSuccessMessage( "プレイヤーが見つかりませんでした")
            return
        }
        if (VehicleManager.hasVehicle(targetUUID)) {
            VehicleManager.pop(targetUUID)
            player.sendVehiclePrefixedSuccessMessage("$name の乗り物をアイテム化しました")
        } else {
            player.sendVehiclePrefixedErrorMessage("$name の乗り物をアイテム化できませんでした")
        }
    }
}