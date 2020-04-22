package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.Permission
import dev.sky_lock.pocketlifevehicle.extension.chat.sendPrefixedPluginMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

/**
 * @author sky_lock
 */
class HelpCommand : ICommand {
    override fun execute(sender: CommandSender, cmd: Command, args: Array<String>) {
        sender.sendPrefixedPluginMessage("-----------コマンドヘルプ-------------")
        sender.sendPrefixedPluginMessage("/vehicle - 利用可能なコマンドを表示します")
        if (Permission.ADMIN_COMMAND.obtained(sender)) {
            sender.sendPrefixedPluginMessage("/vehicle world [add|remove|list] - ワールドで乗り物が使用できるかどうかを設定します")
            sender.sendPrefixedPluginMessage("/vehicle give [target] [modelId] - モデルアイテムをプレイヤーにインベントリーに追加します")
            sender.sendPrefixedPluginMessage("/vehicle spawn [target] [modelId] - モデルをプレイヤーの位置にスポーンさせます")
            sender.sendPrefixedPluginMessage("/vehicle search [target] - 乗り物の現在地を表示します")
            sender.sendPrefixedPluginMessage("/vehicle towaway [target] - プレイヤーの乗り物をアイテム化します")
            sender.sendPrefixedPluginMessage("/vehicle model - モデルエディタを開きます")
            sender.sendPrefixedPluginMessage("/vehicle reload - ファイルからモデル情報を読み書きします")
            sender.sendPrefixedPluginMessage("/vehicle debug - デバッグ用コマンド")
            sender.sendPrefixedPluginMessage("-----------------------------------")
            return
        }
        sender.sendPrefixedPluginMessage("/vehicle search - モデルの現在地を表示します")
        sender.sendPrefixedPluginMessage("/vehicle towaway - 乗り物をアイテム化します")
        sender.sendPrefixedPluginMessage("-----------------------------------")

    }
}