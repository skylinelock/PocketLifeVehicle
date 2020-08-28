package dev.sky_lock.pocketlifevehicle.command.new.node

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import dev.sky_lock.pocketlifevehicle.command.new.Command
import dev.sky_lock.pocketlifevehicle.command.new.context.CommandContextBuilder
import org.bukkit.permissions.Permission

/**
 * @author sky_lock
 */

class ArgumentCommandNode<T>(
    override val name: String,
    val argumentType: ArgumentType<T>,
    permission: Permission?,
    cmd: Command?
) : BukkitCommandNode(permission, cmd) {

    override fun parse(word: String, contextBuilder: CommandContextBuilder) {
        val result = argumentType.parse(StringReader(word))
        // contextBuilder.withArgument(name, )
    }
}