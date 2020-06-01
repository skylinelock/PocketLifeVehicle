package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.extension.chat.sendVehiclePrefixedMessage
import dev.sky_lock.pocketlifevehicle.vehicle.ModelRegistry
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleManager
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */
class SpawnCommand : ICommand, IAdminCommand {
    override fun execute(sender: CommandSender, cmd: Command, args: Array<String>) {
        val player = sender as Player
        if (args.size < 3) {
            player.sendVehiclePrefixedMessage(ChatColor.RED + "引数が足りません")
            return
        }
        val name = args[1]
        val target = Bukkit.getPlayer(name)
        if (target == null) {
            player.sendVehiclePrefixedMessage(ChatColor.RED + "プレイヤーが見つかりませんでした")
            return
        }
        if (!VehiclePlugin.instance.pluginConfiguration.isWorldVehicleCanPlaced(target.world)) {
            player.sendVehiclePrefixedMessage(ChatColor.RED + "対象のプレイヤーがいるワールドは乗り物の使用が許可されていません")
            return
        }
        val id = args[2]
        val model = ModelRegistry.findById(id)
        if (model == null) {
            player.sendVehiclePrefixedMessage(ChatColor.RED + "モデルが見つかりませんでした")
            return
        }
        if (!VehicleManager.verifyPlaceableLocation(target.location)) {
            player.sendVehiclePrefixedMessage(ChatColor.RED + "対象のプレイヤーの位置に乗り物を設置することができませんでした")
            return
        }
        VehicleManager.kill(target.uniqueId)
        val success = VehicleManager.placeEntity(target.uniqueId, model, target.location, model.spec.maxFuel)
        if (success) {
            player.sendVehiclePrefixedMessage(ChatColor.GREEN + name + " に " + id + " を渡しました")
            target.sendVehiclePrefixedMessage(ChatColor.GREEN + "乗り物を受け取りました")
        } else {
            player.sendVehiclePrefixedMessage(ChatColor.RED + "乗り物を設置できませんでした")
        }
    }
}