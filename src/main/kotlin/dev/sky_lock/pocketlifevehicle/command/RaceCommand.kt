package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.extension.chat.sendRacePrefixedMessage
import dev.sky_lock.pocketlifevehicle.extension.chat.sendVehiclePrefixedMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */

class RaceCommand : ICommand, IAdminCommand {

    override fun execute(sender: CommandSender, cmd: Command, args: Array<String>) {
        val player = sender as Player
        if (args.size < 2) {
            player.sendRacePrefixedMessage("/vehicle race [join/leave/start/end]")
            return
        }

    }
}