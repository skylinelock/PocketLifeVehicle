package dev.sky_lock.mocar.command;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author sky_lock
 */

public class HelpCommand implements ICommand {

    @Override
    public void execute(CommandSender sender, Command cmd, String[] args) {
        sender.sendMessage(MoCar.PREFIX + "-----------Help-------------");
        sender.sendMessage(MoCar.PREFIX + "/mocar - 利用可能なコマンドを表示します");
        if (Permission.ADMIN_COMMAND.obtained(sender)) {
            sender.sendMessage(MoCar.PREFIX + "/mocar give [target] [carId] - 指定した車種の車を指定したプレイヤーの位置にスポーンさせます");
            sender.sendMessage(MoCar.PREFIX + "/mocar search [target] - 所有している車の現在地を表示します");
            sender.sendMessage(MoCar.PREFIX + "/mocar toaway [target] - 指定したプレイヤーの車をアイテム化します");
            sender.sendMessage(MoCar.PREFIX + "/mocar edit - カーモデルエディタを開きます");
            sender.sendMessage(MoCar.PREFIX + "/mocar removemodel(rm) [carId] - 指定したidの車種を削除します");
            sender.sendMessage(MoCar.PREFIX + "/mocar listmodel(lm) - 全ての車種を表示します");
            sender.sendMessage(MoCar.PREFIX + "/mocar reload - CarModelコンフィグから車種情報を読み込みます");
            sender.sendMessage(MoCar.PREFIX + "/mocar debug - デバッグ用コマンド");
            sender.sendMessage(MoCar.PREFIX + "----------------------------");
            return;
        }
        sender.sendMessage(MoCar.PREFIX + "/mocar search - 所有している車の現在地を表示します");
        sender.sendMessage(MoCar.PREFIX + "/mocar towaway - 所有している車をアイテム化します");
        sender.sendMessage(MoCar.PREFIX + "----------------------------");
    }
}
