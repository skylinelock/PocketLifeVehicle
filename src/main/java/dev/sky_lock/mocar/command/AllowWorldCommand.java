package dev.sky_lock.mocar.command;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.config.PluginConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class AllowWorldCommand implements ICommand, IAdminCommand {

    @Override
    public void execute(CommandSender sender, Command cmd, String[] args) {
        Player player = (Player) sender;
        PluginConfig config = MoCar.getInstance().getPluginConfig();
        String worldName = player.getWorld().getName();
        if (config.getAllowWorlds().contains(player.getWorld())) {
            config.removeAllWorld(worldName);
            player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "このワールドを車が使用できないようにしました");
            return;
        }
        config.addAllowWorld(worldName);
        player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "このワールドを車が使用できるようにしました");
    }
}
