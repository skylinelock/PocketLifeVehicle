package dev.sky_lock.pocketlifevehicle.command.new.node

import dev.sky_lock.pocketlifevehicle.command.new.CommandRunnable

/**
 * @author sky_lock
 */

class LiteralCommandNode(literal: String, val aliases: List<String>, val description: String, val permission: String, cmd: CommandRunnable?): BukkitCommandNode(cmd) {
    override val name: String = literal
}