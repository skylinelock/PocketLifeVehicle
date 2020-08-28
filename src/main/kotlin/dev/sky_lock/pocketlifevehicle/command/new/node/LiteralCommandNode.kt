package dev.sky_lock.pocketlifevehicle.command.new.node

import dev.sky_lock.pocketlifevehicle.command.new.Command
import dev.sky_lock.pocketlifevehicle.command.new.context.CommandContextBuilder
import org.bukkit.permissions.Permission

/**
 * @author sky_lock
 */

class LiteralCommandNode(
    literal: String, val aliases: List<String>, val description: String,
    permission: Permission?, cmd: Command?
): BukkitCommandNode(permission, cmd) {
    override val name: String = literal

    override fun parse(word: String, contextBuilder: CommandContextBuilder) {
        TODO("Not yet implemented")
    }
}