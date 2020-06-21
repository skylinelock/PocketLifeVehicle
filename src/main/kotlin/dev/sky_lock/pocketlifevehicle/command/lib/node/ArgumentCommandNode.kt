package dev.sky_lock.pocketlifevehicle.command.lib.node

import com.mojang.brigadier.arguments.ArgumentType
import dev.sky_lock.pocketlifevehicle.command.lib.ICommand

/**
 * @author sky_lock
 */

class ArgumentCommandNode<T>(
    override val name: String, cmd: ICommand?, argumentType: ArgumentType<T>
) : BukkitCommandNode(cmd) {

}