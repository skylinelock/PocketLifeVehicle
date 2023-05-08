package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.extension.chat.sendVehiclePrefixedErrorMessage
import dev.sky_lock.pocketlifevehicle.extension.chat.sendVehiclePrefixedSuccessMessage
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleManager
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
            player.sendVehiclePrefixedErrorMessage( "引数が足りません")
            return
        }
        when(args[1].lowercase()) {
            "clear" -> {
                VehicleManager.removeEventVehicles()
                player.sendVehiclePrefixedSuccessMessage( "全てのイベント車両を削除しました")
            }
            "unlock" -> {
                VehicleManager.setLockForEventVehicles(false)
                player.sendVehiclePrefixedSuccessMessage( "全てのイベント車両をアンロックしました")
            }
            "lock" -> {
                VehicleManager.setLockForEventVehicles(true)
                player.sendVehiclePrefixedSuccessMessage( "全てのイベント車両をロックしました")
            }
        }
    }
}