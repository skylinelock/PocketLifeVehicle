package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.extension.chat.sendVehiclePrefixedErrorMessage
import dev.sky_lock.pocketlifevehicle.extension.chat.sendVehiclePrefixedSuccessMessage
import dev.sky_lock.pocketlifevehicle.vehicle.ModelRegistry
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GiveCommand : ICommand, IAdminCommand {

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
        if (model.flag.eventOnly) {
            player.sendVehiclePrefixedErrorMessage( "イベント専用車両にはspawnコマンドを使って下さい")
            return
        }
        if (args.size == 2) {
            player.inventory.addItem(model.itemStack)
            player.sendVehiclePrefixedSuccessMessage( id + "を取得しました")
            return
        }
        val name = args[2]
        val target = Bukkit.getPlayer(name)
        if (target == null) {
            player.sendVehiclePrefixedErrorMessage( "プレイヤーが見つかりませんでした")
            return
        }
        target.inventory.addItem(model.itemStack)
        target.sendVehiclePrefixedSuccessMessage( "乗り物を受け取りました")
        player.sendVehiclePrefixedSuccessMessage( name + "に" + id + "を与えました")
    }
}