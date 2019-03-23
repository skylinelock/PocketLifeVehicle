package dev.sky_lock.mocar.command;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.CarEntities;
import dev.sky_lock.mocar.car.CarModel;
import dev.sky_lock.mocar.car.ModelList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class GiveCommand implements ICommand, IAdminCommand {

    @Override
    public void execute(CommandSender sender, Command cmd, String[] args) {
        Player player = (Player) sender;
        if (args.length < 3) {
            player.sendMessage(MoCar.PREFIX + ChatColor.RED + "引数が足りません");
            return;
        }
        String name = args[1];
        Player target = Bukkit.getPlayer(name);
        if (target == null) {
            player.sendMessage(MoCar.PREFIX + ChatColor.RED + "プレイヤーが見つかりませんでした");
            return;
        }
        String id = args[2];
        CarModel model = ModelList.get(id);
        if (model == null) {
            player.sendMessage(MoCar.PREFIX + ChatColor.RED + "車種が見つかりませんでした");
            return;
        }
        if (!MoCar.getInstance().getPluginConfig().getAllowWorlds().contains(target.getWorld())) {
            player.sendMessage(MoCar.PREFIX + ChatColor.RED + "対象のプレイヤーがいるワールドは車の使用が許可されていません");
            return;
        }
        CarEntities.kill(player.getUniqueId());
        boolean success = CarEntities.spawn(target.getUniqueId(), model, target.getLocation(), model.getMaxFuel());

        if (success) {
            player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "Player: " + name + " の位置に車(ID: " + id + ")を設置しました");
            target.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "車を受け取りました");
        } else {
            player.sendMessage(MoCar.PREFIX + ChatColor.RED + "車を設置できませんでした");
        }
    }
}