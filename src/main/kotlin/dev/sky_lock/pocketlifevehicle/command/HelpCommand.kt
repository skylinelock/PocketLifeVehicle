package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.PLVehicle
import dev.sky_lock.pocketlifevehicle.Permission
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

/**
 * @author sky_lock
 */
class HelpCommand : ICommand {
    override fun execute(sender: CommandSender, cmd: Command, args: Array<String>) {
        sender.sendMessage(PLVehicle.PREFIX + "-----------Help-------------")
        sender.sendMessage(PLVehicle.PREFIX + "/vehicle - 利用可能なコマンドを表示します")
        if (Permission.ADMIN_COMMAND.obtained(sender)) {
            sender.sendMessage(PLVehicle.PREFIX + "/vehicle give [target] [modelId] - モデルアイテムをプレイヤーにインベントリーに追加します")
            sender.sendMessage(PLVehicle.PREFIX + "/vehicle spawn [target] [modelId] - モデルをプレイヤーの位置にスポーンさせます")
            sender.sendMessage(PLVehicle.PREFIX + "/vehicle search [target] - 乗り物の現在地を表示します")
            sender.sendMessage(PLVehicle.PREFIX + "/vehicle towaway [target] - プレイヤーの乗り物をアイテム化します")
            sender.sendMessage(PLVehicle.PREFIX + "/vehicle model - モデルエディタを開きます")
            sender.sendMessage(PLVehicle.PREFIX + "/vehicle reload - ファイルからモデル情報を読み書きします")
            sender.sendMessage(PLVehicle.PREFIX + "/vehicle debug - デバッグ用コマンド")
            sender.sendMessage(PLVehicle.PREFIX + "----------------------------")
            return
        }
        sender.sendMessage(PLVehicle.PREFIX + "/vehicle search - モデルの現在地を表示します")
        sender.sendMessage(PLVehicle.PREFIX + "/vehicle towaway - 乗り物をアイテム化します")
        sender.sendMessage(PLVehicle.PREFIX + "----------------------------")
    }
}