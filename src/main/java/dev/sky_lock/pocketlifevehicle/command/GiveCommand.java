package dev.sky_lock.pocketlifevehicle.command;

import dev.sky_lock.pocketlifevehicle.PLVehicle;
import dev.sky_lock.pocketlifevehicle.car.ModelList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveCommand implements ICommand, IAdminCommand {

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
            player.getInventory().addItem(model.getItemStack());
            player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + name + "に" + id + "を与えました");
            target.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "車を受け取りました");
            return model;
        }).orElseGet(() -> {
            player.sendMessage(PLVehicle.PREFIX + ChatColor.RED + "車種が見つかりませんでした");
            return null;
        });
    }
}
