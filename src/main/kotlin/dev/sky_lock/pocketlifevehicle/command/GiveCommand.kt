package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.extension.chat.sendVehiclePrefixedMessage
import dev.sky_lock.pocketlifevehicle.vehicle.ModelRegistry
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GiveCommand : ICommand, IAdminCommand {

    override fun execute(sender: CommandSender, cmd: Command, args: Array<String>) {
        val player = sender as Player
        if (args.size < 2) {
            player.sendVehiclePrefixedMessage(ChatColor.RED + "引数が足りません")
            return
        }
        val id = args[1]
        val model = ModelRegistry.findById(id)
        if (model == null) {
            player.sendVehiclePrefixedMessage(ChatColor.RED + "モデルが見つかりませんでした")
            return
        }
        if (model.flag.eventOnly) {
            player.sendVehiclePrefixedMessage(ChatColor.RED + "イベント専用車両にはspawnコマンドを使って下さい")
            return
        }
        if (args.size == 2) {
            player.inventory.addItem(model.itemStack)
            player.sendVehiclePrefixedMessage(ChatColor.GREEN + id + "を取得しました")
            return
        }
        val name = args[2]
        val target = Bukkit.getPlayer(name)
        if (target == null) {
            player.sendVehiclePrefixedMessage(ChatColor.RED + "プレイヤーが見つかりませんでした")
            return
        }
        target.inventory.addItem(model.itemStack)
        target.sendVehiclePrefixedMessage(ChatColor.GREEN + "乗り物を受け取りました")
        player.sendVehiclePrefixedMessage(ChatColor.GREEN + name + "に" + id + "を与えました")
    }
}