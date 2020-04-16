package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.PLVehicle
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */
class AllowWorldCommand : ICommand, IAdminCommand {
    override fun execute(sender: CommandSender, cmd: Command, args: Array<String>) {
        val player = sender as Player
        val config = PLVehicle.instance.pluginConfiguration
        val worldName = player.world.name
        if (config.getAllowWorlds().contains(player.world)) {
            config.removeAllowWorld(worldName)
            player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "このワールドを車が使用できないようにしました")
            return
        }
        config.addAllowWorld(worldName)
        player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "このワールドを車が使用できるようにしました")
    }
}