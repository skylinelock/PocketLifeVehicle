package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.Permission
import dev.sky_lock.pocketlifevehicle.extensions.plus
import dev.sky_lock.pocketlifevehicle.extensions.sendPrefixedPluginMessage
import dev.sky_lock.pocketlifevehicle.vehicle.Storage
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GiveCommand : ICommand, IAdminCommand {
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
        val id = args[2]
        val model = Storage.MODEL.findById(id)
        if (model == null) {
            player.sendPrefixedPluginMessage(ChatColor.RED + "モデルが見つかりませんでした")
            return
        }
        target.inventory.addItem(model.itemStack)
        player.sendPrefixedPluginMessage(ChatColor.GREEN + name + "に" + id + "を与えました")
        if (Permission.ADMIN_COMMAND.obtained(target)) {
            target.sendPrefixedPluginMessage(ChatColor.GREEN + "乗り物を受け取りました")
        } else {
            target.sendPrefixedPluginMessage(ChatColor.GREEN + "乗り物を受け取りました")
        }
    }
}