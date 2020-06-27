package dev.sky_lock.pocketlifevehicle.command.new.node

import com.mojang.brigadier.arguments.ArgumentType
import dev.sky_lock.pocketlifevehicle.command.new.CommandRunnable
import org.bukkit.permissions.Permission

/**
 * @author sky_lock
 */

class ArgumentCommandNode<T>(
    override val name: String,
    argumentType: ArgumentType<T>,
    permission: Permission?,
    cmd: CommandRunnable?
) : BukkitCommandNode(permission, cmd) {

}