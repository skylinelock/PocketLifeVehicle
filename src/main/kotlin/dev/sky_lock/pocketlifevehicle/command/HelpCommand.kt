package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.Permission
import dev.sky_lock.pocketlifevehicle.extension.chat.sendVehiclePrefixedMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

/**
 * @author sky_lock
 */
class HelpCommand : ICommand {
    override fun execute(sender: CommandSender, cmd: Command, args: Array<String>) {
        sender.sendVehiclePrefixedMessage("-----------コマンドヘルプ-------------")
        sender.sendVehiclePrefixedMessage("/vehicle - 利用可能なコマンドを表示します")
        if (Permission.ADMIN_COMMAND.obtained(sender)) {
            sender.sendVehiclePrefixedMessage("/vehicle world [add|remove|list] - ワールドで乗り物が使用できるかどうかを設定します")
            sender.sendVehiclePrefixedMessage("/vehicle give [modelId] [target] - モデルアイテムをプレイヤーにインベントリーに追加します")
            sender.sendVehiclePrefixedMessage("/vehicle spawn [modelId] [target] - モデルをプレイヤーの位置にスポーンさせます")
            sender.sendVehiclePrefixedMessage("/vehicle search [target] - 乗り物の現在地を表示します")
            sender.sendVehiclePrefixedMessage("/vehicle pop [target] - プレイヤーの乗り物をアイテム化します")
            sender.sendVehiclePrefixedMessage("/vehicle model - モデルエディタを開きます")
            sender.sendVehiclePrefixedMessage("/vehicle reload - ファイルからモデル情報を読み書きします")
            sender.sendVehiclePrefixedMessage("/vehicle debug - デバッグ用コマンド")
            sender.sendVehiclePrefixedMessage("-----------------------------------")
            return
        }
        sender.sendVehiclePrefixedMessage("/vehicle search - モデルの現在地を表示します")
        sender.sendVehiclePrefixedMessage("/vehicle pop - 乗り物をアイテム化します")
        sender.sendVehiclePrefixedMessage("-----------------------------------")

    }
}