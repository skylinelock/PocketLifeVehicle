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
            player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "Id             : " + model.getId());
            player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "Name          : " + model.getName());
            player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "Lore          : " + model.getLores());
            player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "Speed        : " + model.getMaxSpeed().getLabel());
            player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "MaxFuel     : " + model.getMaxFuel());
            player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "Capacity     : " + model.getCapacity().value());
            player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "Collision     : " + model.getCollideBox().getBaseSide() + ":" + model.getCollideBox().getHeight());
            player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "Height        : " + model.getHeight());
            player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "-------------------------------------------");
        });
    }
}
