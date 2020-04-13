package dev.sky_lock.pocketlifevehicle.command

import org.bukkit.command.Command
import org.bukkit.command.CommandSender

/**
 * @author sky_lock
 */
interface ICommand {
    fun execute(sender: CommandSender, cmd: Command, args: Array<String>)
}