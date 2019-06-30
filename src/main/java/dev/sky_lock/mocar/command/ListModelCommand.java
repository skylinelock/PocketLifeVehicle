package dev.sky_lock.mocar.command;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.ModelList;
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
            player.sendMessage(MoCar.PREFIX + ChatColor.RED + "モデルが見つかりませんでした");
            return;
        }
        ModelList.unmodified().forEach(model -> {
            player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "-------------------------------------------");
            player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "Id             : " + model.getId());
            player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "Name          : " + model.getName());
            player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "Lore          : " + model.getLores());
            player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "Speed        : " + model.getMaxSpeed().getLabel());
            player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "MaxFuel     : " + model.getMaxFuel());
            player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "Capacity     : " + model.getCapacity().value());
            player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "Collision     : " + model.getCollideBox().getBaseSide() + ":" + model.getCollideBox().getHeight());
            player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "Height        : " + model.getHeight());
            player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "-------------------------------------------");
        });
    }
}
