package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.Permission
import dev.sky_lock.pocketlifevehicle.extension.chat.sendVehiclePrefixedRawMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

/**
 * @author sky_lock
 */
class HelpCommand : ICommand {
    override fun execute(sender: CommandSender, cmd: Command, args: Array<String>) {
        sender.sendVehiclePrefixedRawMessage("-----------コマンドヘルプ-------------")
        sender.sendVehiclePrefixedRawMessage("/vehicle - 利用可能なコマンドを表示します")
        if (Permission.ADMIN_COMMAND.obtained(sender)) {
            sender.sendVehiclePrefixedRawMessage("/vehicle world (add|remove|list) - ワールドで乗り物が使用できるかどうかを設定します")
            sender.sendVehiclePrefixedRawMessage("/vehicle give [modelId] [target] - モデルアイテムをプレイヤーにインベントリーに追加します")
            sender.sendVehiclePrefixedRawMessage("/vehicle spawn [modelId] [target] - モデルをプレイヤーの位置にスポーンさせます")
            sender.sendVehiclePrefixedRawMessage("/vehicle search [target] - 乗り物の現在地を表示します")
            sender.sendVehiclePrefixedRawMessage("/vehicle forgive [target] - プレイヤーの駐車違反登録を解除します")
            sender.sendVehiclePrefixedRawMessage("/vehicle pop [target] - プレイヤーの乗り物をアイテム化します")
            sender.sendVehiclePrefixedRawMessage("/vehicle model - モデルエディタを開きます")
            sender.sendVehiclePrefixedRawMessage("/vehicle event (clear|unlock|lock) - イベント車両用コマンドです")
            sender.sendVehiclePrefixedRawMessage("/vehicle reload - ファイルからモデル情報を読み書きします")
            sender.sendVehiclePrefixedRawMessage("/vehicle debug - デバッグ用コマンド")
            sender.sendVehiclePrefixedRawMessage("-----------------------------------")
            return
        }
        sender.sendVehiclePrefixedRawMessage("/vehicle search - モデルの現在地を表示します")
        sender.sendVehiclePrefixedRawMessage("/vehicle pop - 乗り物をアイテム化します")
        sender.sendVehiclePrefixedRawMessage("-----------------------------------")

    }
}