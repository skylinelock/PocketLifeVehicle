package dev.sky_lock.mocar.commands;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.CarEntities;
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
        CarEntities.kill(player.getUniqueId());
        boolean success = CarEntities.spawn(target.getUniqueId(), ModelList.get(id), target.getLocation());

        if (success) {
            player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "Player:'" + name + "'の位置に車(ID:'" + id + "')を設置しました");
            target.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "車(ID:'" + id + "')を受け取りました");
        } else {
            player.sendMessage(MoCar.PREFIX + ChatColor.RED + "車を設置できませんでした");
        }
    }
}
