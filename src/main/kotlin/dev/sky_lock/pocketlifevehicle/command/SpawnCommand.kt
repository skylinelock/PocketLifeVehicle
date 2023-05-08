package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import dev.sky_lock.pocketlifevehicle.extension.chat.sendVehiclePrefixedErrorMessage
import dev.sky_lock.pocketlifevehicle.extension.chat.sendVehiclePrefixedSuccessMessage
import dev.sky_lock.pocketlifevehicle.vehicle.ModelRegistry
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleManager
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */
class SpawnCommand : ICommand, IAdminCommand {
    override fun execute(sender: CommandSender, cmd: Command, args: Array<String>) {
        val player = sender as Player
        if (args.size < 2) {
            player.sendVehiclePrefixedErrorMessage( "引数が足りません")
            return
        }
        val id = args[1]
        val model = ModelRegistry.findById(id)
        if (model == null) {
            player.sendVehiclePrefixedErrorMessage( "モデルが見つかりませんでした")
            return
        }
        if (args.size == 2) {
            if (!VehiclePlugin.instance.pluginConfiguration.isWorldVehicleCanPlaced(player.world)) {
                player.sendVehiclePrefixedErrorMessage( "このワールドは乗り物の使用が許可されていません")
                return
            }
            if (model.flag.eventOnly) {
                VehicleManager.placeEventVehicle(player.location, model)
                player.sendVehiclePrefixedSuccessMessage( "イベント専用車両を設置しました")
                return
            }
            if (!VehicleManager.verifyPlaceableLocation(player.location)) {
                player.sendVehiclePrefixedErrorMessage( "この位置に乗り物は設置できません")
                return
            }
            VehicleManager.remove(player.uniqueId)
            val success = VehicleManager.placeVehicle(player.uniqueId, player.location, model, model.spec.maxFuel)
            if (success) {
                player.sendVehiclePrefixedSuccessMessage( id + " を取得しました")
            } else {
                player.sendVehiclePrefixedErrorMessage( "乗り物を設置できませんでした")
            }
            return
        }
        val name = args[2]
        val target = Bukkit.getPlayer(name)
        if (target == null) {
            player.sendVehiclePrefixedErrorMessage( "プレイヤーが見つかりませんでした")
            return
        }
        if (!VehiclePlugin.instance.pluginConfiguration.isWorldVehicleCanPlaced(target.world)) {
            player.sendVehiclePrefixedErrorMessage( "対象のプレイヤーがいるワールドは乗り物の使用が許可されていません")
            return
        }
        if (model.flag.eventOnly) {
            VehicleManager.placeEventVehicle(target.location, model)
            player.sendVehiclePrefixedSuccessMessage( name + "の位置にイベント専用車両を設置しました")
            return
        }
        if (!VehicleManager.verifyPlaceableLocation(target.location)) {
            player.sendVehiclePrefixedErrorMessage( "対象のプレイヤーの位置に乗り物を設置することができませんでした")
            return
        }
        val success = VehicleManager.placeVehicle(target.uniqueId, target.location, model, model.spec.maxFuel)
        if (success) {
            player.sendVehiclePrefixedSuccessMessage( name + " に " + id + " を渡しました")
            target.sendVehiclePrefixedSuccessMessage( "乗り物を受け取りました")
        } else {
            player.sendVehiclePrefixedErrorMessage( "乗り物を設置できませんでした")
        }
    }
}