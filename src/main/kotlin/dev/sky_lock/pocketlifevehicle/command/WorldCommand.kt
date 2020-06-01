package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.extension.chat.sendVehiclePrefixedMessage
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */
class WorldCommand : ICommand, IAdminCommand {
    override fun execute(sender: CommandSender, cmd: Command, args: Array<String>) {
        val player = sender as Player
        val config = VehiclePlugin.instance.pluginConfiguration

        if (args.size < 2 || args[1].equals("list", ignoreCase = true)) {
            Bukkit.getWorlds().forEach{world ->
                if (config.isWorldVehicleCanPlaced(world)) {
                    player.sendVehiclePrefixedMessage(ChatColor.GREEN + "- " + world.name)
                } else {
                    player.sendVehiclePrefixedMessage("- " + world.name)
                }
            }
            return
        }
        val uid = player.world.uid
        when (args[1]) {
            "add" -> {
                config.setWorldVehicleCanPlaced(uid, true)
                player.sendVehiclePrefixedMessage(ChatColor.GREEN + "このワールドでの乗り物の使用を許可しました")
            }
            "remove" -> {
                config.setWorldVehicleCanPlaced(uid, false)
                player.sendVehiclePrefixedMessage(ChatColor.GREEN + "このワールドでの乗り物の使用をできないようにしました")
            }
            else -> {
                player.sendVehiclePrefixedMessage("/vehicle world [add|remove|list]")
            }
        }
    }
}