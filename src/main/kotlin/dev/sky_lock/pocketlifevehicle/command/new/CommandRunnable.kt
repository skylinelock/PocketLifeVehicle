package dev.sky_lock.pocketlifevehicle.command.new

import org.bukkit.command.Command
import org.bukkit.command.CommandSender

/**
 * @author sky_lock
 */

interface CommandRunnable {

    fun run(sender: CommandSender, command: Command, label: String, args: Array<String>): Int
}