package dev.sky_lock.pocketlifevehicle.command;

import dev.sky_lock.pocketlifevehicle.PLVehicle;
import dev.sky_lock.pocketlifevehicle.car.ModelList;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class ListModelCommand implements ICommand {

    @Override
    public void execute(CommandSender sender, Command cmd, String[] args) {
        Player player = (Player) sender;
        if (ModelList.unmodified().isEmpty()) {
            player.sendMessage(PLVehicle.PREFIX + ChatColor.RED + "モデルが見つかりませんでした");
            return;
        }
        ModelList.unmodified().forEach(model -> {
            player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "-------------------------------------------");
            player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "ID            : " + model.getId());
            player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "名前          : " + model.getName());
            player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "説明          : " + model.getLores());
            player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "最高速度      : " + model.getMaxSpeed().getLabel());
            player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "燃料上限     : " + model.getMaxFuel());
            player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "乗車人数     : " + model.getCapacity().value());
            player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "当たり判定   : " + model.getCollideBox().getBaseSide() + ":" + model.getCollideBox().getHeight());
            player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "座高         : " + model.getHeight());
            player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "-------------------------------------------");
        });
    }
}
