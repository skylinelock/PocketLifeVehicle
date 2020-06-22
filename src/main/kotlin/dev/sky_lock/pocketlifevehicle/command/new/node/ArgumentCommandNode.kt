package dev.sky_lock.pocketlifevehicle.command.new.node

import com.mojang.brigadier.arguments.ArgumentType
import dev.sky_lock.pocketlifevehicle.command.new.CommandRunnable

/**
 * @author sky_lock
 */

class ArgumentCommandNode<T>(
    override val name: String, cmd: CommandRunnable?, argumentType: ArgumentType<T>
) : BukkitCommandNode(cmd) {

}