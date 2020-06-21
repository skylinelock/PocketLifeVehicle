package dev.sky_lock.pocketlifevehicle.command.lib.node

import dev.sky_lock.pocketlifevehicle.command.lib.ICommand

/**
 * @author sky_lock
 */

class LiteralCommandNode(literal: String, cmd: ICommand?): BukkitCommandNode(cmd) {
    override val name: String = literal
}