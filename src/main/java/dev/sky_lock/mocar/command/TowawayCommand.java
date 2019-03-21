package dev.sky_lock.mocar.command;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.Permission;
import dev.sky_lock.mocar.car.CarArmorStand;
import dev.sky_lock.mocar.car.CarEntities;
import dev.sky_lock.mocar.util.MojangUtil;
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
                player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "所有する車をアイテム化しました");
            } else {
                player.sendMessage(MoCar.PREFIX + ChatColor.RED + "車を所有していません");
            }
            return;
        }
        String name = args[1];
        UUID targetUUID = MojangUtil.getUUID(name);
        if (towaway(targetUUID)) {
            player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "Player: " + name + " の車をアイテム化しました");
        } else {
            player.sendMessage(MoCar.PREFIX + ChatColor.RED + "Player: " + name + " は車を所持していません");
        }
    }

    private boolean towaway(UUID uuid) {
        CarArmorStand carEntity = CarEntities.get(uuid);
        if (carEntity == null) {
            return false;
        }
        CarEntities.tow(uuid);
        return true;
    }
}
