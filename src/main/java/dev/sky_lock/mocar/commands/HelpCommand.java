package dev.sky_lock.mocar.commands;

import dev.sky_lock.mocar.MoCar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author sky_lock
 */

public class HelpCommand implements ICommand {

    @Override
    public void execute(CommandSender sender, Command cmd, String[] args) {
        sender.sendMessage(MoCar.PREFIX + "-----------Help-------------");
        sender.sendMessage(MoCar.PREFIX + "/mocar (help) - 利用可能なコマンドを表示します");
        sender.sendMessage(MoCar.PREFIX + "/mocar give [id] [target] - 指定した車種の車を指定したプレイヤーの位置にスポーンさせます");
        sender.sendMessage(MoCar.PREFIX + "/mocar search - 所有している車の現在地を表示します");
        sender.sendMessage(MoCar.PREFIX + "/mocar toaway [target] - 所有者情報を保持した状態で指定したプレイヤーの車をアイテム化します");
        sender.sendMessage(MoCar.PREFIX + "/mocar addmodel [id] [name] [lore] [maxfuel] [distance] [speed] - なんとかします");
        sender.sendMessage(MoCar.PREFIX + "/mocar removemodel [id] - 指定したidの車種を削除します");
        sender.sendMessage(MoCar.PREFIX + "/mocar listmodel [id] - 全ての車種を表示します");
        sender.sendMessage(MoCar.PREFIX + "/mocar reload - cars.ymlから車種情報を読み込みます");
        sender.sendMessage(MoCar.PREFIX + "/mocar debug - デバッグ用コマンド");
        sender.sendMessage(MoCar.PREFIX + "----------------------------");
    }
}
