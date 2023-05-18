package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import dev.sky_lock.pocketlifevehicle.text.ext.sendVehiclePrefixedRawMessage
import dev.sky_lock.pocketlifevehicle.text.ext.sendVehiclePrefixedSuccessMessage
import org.bukkit.Bukkit
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
                    player.sendVehiclePrefixedSuccessMessage( "- " + world.name)
                } else {
                    player.sendVehiclePrefixedRawMessage("- " + world.name)
                }
            }
            return
        }
        val uid = player.world.uid
        when (args[1]) {
            "add" -> {
                config.setWorldVehicleCanPlaced(uid, true)
                player.sendVehiclePrefixedSuccessMessage( "このワールドでの乗り物の使用を許可しました")
            }
            "remove" -> {
                config.setWorldVehicleCanPlaced(uid, false)
                player.sendVehiclePrefixedSuccessMessage( "このワールドでの乗り物の使用をできないようにしました")
            }
            else -> {
                player.sendVehiclePrefixedRawMessage("/vehicle world [add|remove|list]")
            }
        }
    }
}