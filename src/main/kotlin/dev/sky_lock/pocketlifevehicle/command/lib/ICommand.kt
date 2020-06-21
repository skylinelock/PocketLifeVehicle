package dev.sky_lock.pocketlifevehicle.command.lib

import org.bukkit.command.Command
import org.bukkit.command.CommandSender

/**
 * @author sky_lock
 */

interface ICommand {

    fun run(sender: CommandSender, command: Command, label: String, args: Array<String>): Int
}