package dev.sky_lock.pocketlifevehicle.command;

import dev.sky_lock.pocketlifevehicle.PLVehicle;
import dev.sky_lock.pocketlifevehicle.Permission;
import dev.sky_lock.pocketlifevehicle.util.Profiles;
import dev.sky_lock.pocketlifevehicle.vehicle.Car;
import dev.sky_lock.pocketlifevehicle.vehicle.CarEntities;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author sky_lock
 */

public class TowawayCommand implements ICommand {

    @Override
    public void execute(CommandSender sender, Command cmd, String[] args) {
        Player player = (Player) sender;
        if (!Permission.ADMIN_COMMAND.obtained(sender) || args.length < 2) {
            if (towaway(player.getUniqueId())) {
                player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "所有する車をアイテム化しました");
            } else {
                player.sendMessage(PLVehicle.PREFIX + ChatColor.RED + "車を所有していません");
            }
            return;
        }
        String name = args[1];
        UUID targetUUID = Profiles.fetchUUID(name);
        if (towaway(targetUUID)) {
            player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "Player: " + name + " の車をアイテム化しました");
        } else {
            player.sendMessage(PLVehicle.PREFIX + ChatColor.RED + "Player: " + name + " は車を所持していません");
        }
    }

    private boolean towaway(UUID uuid) {
        Car carEntity = CarEntities.of(uuid);
        if (carEntity == null) {
            return false;
        }
        CarEntities.tow(uuid);
        return true;
    }
}
