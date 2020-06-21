package dev.sky_lock.pocketlifevehicle.command.lib.builder

import dev.sky_lock.pocketlifevehicle.command.lib.node.BukkitCommandNode
import dev.sky_lock.pocketlifevehicle.command.lib.ICommand
import dev.sky_lock.pocketlifevehicle.command.lib.node.RootCommandNode
import org.bukkit.command.Command
import org.bukkit.command.CommandSender


/**
 * @author sky_lock
 */

abstract class ArgumentBuilder {

    protected abstract fun getThis(): ArgumentBuilder
    var command: ICommand? = null
    val rootNode = RootCommandNode()

    fun then(nodeBuilder: ArgumentBuilder): ArgumentBuilder {
        this.rootNode.addChild(nodeBuilder.build())
        return getThis()
    }

    fun executes(command: ICommand): ArgumentBuilder {
        this.command = command
        return getThis()
    }

    fun executes(exec : (sender: CommandSender, cmd: Command, label: String, args: Array<String>) -> Int): ArgumentBuilder {
        this.command = object : ICommand {
            override fun run(sender: CommandSender, command: Command, label: String, args: Array<String>): Int {
                return exec(sender, command, label, args)
            }
        }
        return getThis()
    }

    abstract fun build(): BukkitCommandNode
}