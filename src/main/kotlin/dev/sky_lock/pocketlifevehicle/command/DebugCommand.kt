package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.vehicle.Storage
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

/**
 * @author sky_lock
 */
class DebugCommand : ICommand, IAdminCommand {
    override fun execute(sender: CommandSender, cmd: Command, args: Array<String>) {
         Storage.MODEL.forEach { model -> {
             Bukkit.broadcastMessage(model.id)
         } }
        Bukkit.broadcastMessage(Storage.MODEL.size().toString())
    }
}