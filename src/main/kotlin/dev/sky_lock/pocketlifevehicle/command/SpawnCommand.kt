package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.extension.chat.sendPrefixedPluginMessage
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
            player.sendPrefixedPluginMessage(ChatColor.RED + "引数が足りません")
            return
        }
        val name = args[1]
        val target = Bukkit.getPlayer(name)
        if (target == null) {
            player.sendPrefixedPluginMessage(ChatColor.RED + "プレイヤーが見つかりませんでした")
            return
        }
        if (!VehiclePlugin.instance.pluginConfiguration.isWorldVehicleCanPlaced(target.world)) {
            player.sendPrefixedPluginMessage(ChatColor.RED + "対象のプレイヤーがいるワールドは乗り物の使用が許可されていません")
            return
        }
        val id = args[2]
        val model = ModelRegistry.findById(id)
        if (model == null) {
            player.sendPrefixedPluginMessage(ChatColor.RED + "モデルが見つかりませんでした")
            return
        }
        VehicleManager.kill(target.uniqueId)
        val success = VehicleManager.spawn(target.uniqueId, model, target.location, model.spec.maxFuel)
        if (success) {
            player.sendPrefixedPluginMessage(ChatColor.GREEN + name + " に " + id + " を渡しました")
            target.sendPrefixedPluginMessage(ChatColor.GREEN + "乗り物を受け取りました")
        } else {
            player.sendPrefixedPluginMessage(ChatColor.RED + "乗り物を設置できませんでした")
        }
    }
}