package dev.sky_lock.pocketlifevehicle.command;

import dev.sky_lock.pocketlifevehicle.PLVehicle;
import dev.sky_lock.pocketlifevehicle.vehicle.CarEntities;
import dev.sky_lock.pocketlifevehicle.vehicle.model.ModelList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class SpawnCommand implements ICommand, IAdminCommand {

    @Override
    public void execute(CommandSender sender, Command cmd, String[] args) {
        Player player = (Player) sender;
        if (args.length < 3) {
            player.sendMessage(PLVehicle.PREFIX + ChatColor.RED + "引数が足りません");
            return;
        }
        String name = args[1];
        Player target = Bukkit.getPlayer(name);
        if (target == null) {
            player.sendMessage(PLVehicle.PREFIX + ChatColor.RED + "プレイヤーが見つかりませんでした");
            return;
        }
        String id = args[2];
        ModelList.of(id).map(model -> {
            if (!PLVehicle.getInstance().getPluginConfig().getAllowWorlds().contains(target.getWorld())) {
                player.sendMessage(PLVehicle.PREFIX + ChatColor.RED + "対象のプレイヤーがいるワールドは車の使用が許可されていません");
                return model;
            }
            CarEntities.kill(target.getUniqueId());
            boolean success = CarEntities.spawn(target.getUniqueId(), model, target.getLocation(), model.getSpec().getMaxFuel());

            if (success) {
                player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + name + " に " + id + " を渡しました");
                target.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "車を受け取りました");
            } else {
                player.sendMessage(PLVehicle.PREFIX + ChatColor.RED + "車を設置できませんでした");
            }
            return model;
        }).orElseGet(() -> {
            player.sendMessage(PLVehicle.PREFIX + ChatColor.RED + "車種が見つかりませんでした");
            return null;
        });
    }
}
