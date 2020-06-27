package dev.sky_lock.pocketlifevehicle.command.new.builder

import dev.sky_lock.pocketlifevehicle.command.new.CommandRunnable
import dev.sky_lock.pocketlifevehicle.command.new.node.BukkitCommandNode
import dev.sky_lock.pocketlifevehicle.command.new.node.RootCommandNode
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.permissions.Permission


/**
 * @author sky_lock
 */

abstract class ArgumentBuilder {

    protected abstract fun getThis(): ArgumentBuilder
    var runnable: CommandRunnable? = null
    val rootNode = RootCommandNode()
    var permission: Permission? = null

    fun permission(permission: Permission): ArgumentBuilder {
        this.permission = permission
        return getThis()
    }

    fun then(nodeBuilder: ArgumentBuilder): ArgumentBuilder {
        this.rootNode.addChild(nodeBuilder.build())
        return getThis()
    }

    fun executes(runnable: CommandRunnable): ArgumentBuilder {
        this.runnable = runnable
        return getThis()
    }

    fun executes(exec: (sender: CommandSender, cmd: Command, label: String, args: Array<String>) -> Int): ArgumentBuilder {
        this.runnable = object : CommandRunnable {
            override fun run(sender: CommandSender, command: Command, label: String, args: Array<String>): Int {
                return exec(sender, command, label, args)
            }
        }
        return getThis()
    }

    abstract fun build(): BukkitCommandNode
}