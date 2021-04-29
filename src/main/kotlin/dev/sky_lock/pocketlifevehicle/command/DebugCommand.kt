package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.vehicle.VehicleManager
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

/**
 * @author sky_lock
 */
class DebugCommand : ICommand, IAdminCommand {
    override fun execute(sender: CommandSender, cmd: Command, args: Array<String>) {
        val vehicleMap = VehicleManager.vehicleMap
        vehicleMap.forEach { uuid, vehicle ->
            val player = Bukkit.getPlayer(uuid) ?: return@forEach
            Bukkit.broadcastMessage(player.name + " : " + vehicle.model.id)
        }
    }
}