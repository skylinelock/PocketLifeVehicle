package dev.sky_lock.pocketlifevehicle.command.new.node

import dev.sky_lock.pocketlifevehicle.command.new.CommandRunnable
import org.bukkit.permissions.Permission

/**
 * @author sky_lock
 */

class LiteralCommandNode(
    literal: String, val aliases: List<String>, val description: String,
    permission: Permission?, cmd: CommandRunnable?
): BukkitCommandNode(permission, cmd) {
    override val name: String = literal
}