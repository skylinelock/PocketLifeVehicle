package dev.sky_lock.pocketlifevehicle.command.new.builder

import dev.sky_lock.pocketlifevehicle.command.new.node.BukkitCommandNode
import dev.sky_lock.pocketlifevehicle.command.new.node.RootCommandNode
import org.bukkit.command.CommandSender
import org.bukkit.permissions.Permission


/**
 * @author sky_lock
 */

abstract class ArgumentBuilder {

    protected abstract fun getThis(): ArgumentBuilder
    var runnable: dev.sky_lock.pocketlifevehicle.command.new.Command? = null
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

    fun executes(runnable: dev.sky_lock.pocketlifevehicle.command.new.Command): ArgumentBuilder {
        this.runnable = runnable
        return getThis()
    }

    fun executes(exec: (sender: CommandSender, cmd: org.bukkit.command.Command, label: String, args: Array<String>) -> Int): ArgumentBuilder {
        this.runnable = object : dev.sky_lock.pocketlifevehicle.command.new.Command {
            override fun run(sender: CommandSender, command: org.bukkit.command.Command, label: String, args: Array<String>): Int {
                return exec(sender, command, label, args)
            }
        }
        return getThis()
    }

    abstract fun build(): BukkitCommandNode
}