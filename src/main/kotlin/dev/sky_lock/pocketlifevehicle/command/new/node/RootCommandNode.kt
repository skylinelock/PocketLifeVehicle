package dev.sky_lock.pocketlifevehicle.command.new.node

import dev.sky_lock.pocketlifevehicle.command.new.context.CommandContextBuilder

/**
 * @author sky_lock
 */

class RootCommandNode : BukkitCommandNode(null, null) {
    override val name = "root"

    override fun parse(word: String, contextBuilder: CommandContextBuilder) {
        TODO("Not yet implemented")
    }
}