package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.extension.chat.sendVehiclePrefixedErrorMessage
import dev.sky_lock.pocketlifevehicle.extension.chat.sendVehiclePrefixedSuccessMessage
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleManager
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ForgiveCommand : ICommand, IAdminCommand {

    override fun execute(sender: CommandSender, cmd: Command, args: Array<String>) {
        if (args.size < 2) {
            sender.sendVehiclePrefixedErrorMessage( "引数が足りません")
            return
        }
        val player = sender as Player
        val name = args[1]
        val targetUUID = Bukkit.getPlayerUniqueId(name)
        if (targetUUID == null) {
            player.sendVehiclePrefixedErrorMessage( "プレイヤーが見つかりませんでした")
            return
        }
        if (VehicleManager.unregisterIllegalParking(targetUUID)) {
            player.sendVehiclePrefixedSuccessMessage("$name の駐車違反登録を解除しました")
        } else {
            player.sendVehiclePrefixedErrorMessage("$name は駐車違反登録されていません")
        }
        return
    }
}