package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.PLVehicle
import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.extension.chat.sendPrefixedPluginMessage
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
        val config = PLVehicle.instance.pluginConfiguration

        if (args.size < 2 || args[1].equals("list", ignoreCase = true)) {
            Bukkit.getWorlds().forEach{world ->
                if (config.isWorldVehicleCanPlaced(world)) {
                    player.sendPrefixedPluginMessage(ChatColor.GREEN + "- " + world.name)
                } else {
                    player.sendPrefixedPluginMessage("- " + world.name)
                }
            }
            return
        }
        val uid = player.world.uid
        when (args[1]) {
            "add" -> {
                config.setWorldVehicleCanPlaced(uid, true)
                player.sendPrefixedPluginMessage(ChatColor.GREEN + "このワールドでの乗り物の使用を許可しました")
            }
            "remove" -> {
                config.setWorldVehicleCanPlaced(uid, false)
                player.sendPrefixedPluginMessage(ChatColor.GREEN + "このワールドでの乗り物の使用をできないようにしました")
            }
            else -> {
                player.sendPrefixedPluginMessage("/vehicle world [add|remove|list]")
            }
        }
    }
}