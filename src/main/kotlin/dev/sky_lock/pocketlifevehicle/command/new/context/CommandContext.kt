package dev.sky_lock.pocketlifevehicle.command.new.context

import org.bukkit.command.CommandSender

/**
 * @author sky_lock
 */

class CommandContext(val sender: CommandSender, val commandName: String, val args: Array<String>, arguments: Map<String, ParsedArgument>) {


}