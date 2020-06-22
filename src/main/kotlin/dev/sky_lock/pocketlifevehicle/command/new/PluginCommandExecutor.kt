package dev.sky_lock.pocketlifevehicle.command.new

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import dev.sky_lock.pocketlifevehicle.command.new.builder.ArgumentBuilder
import dev.sky_lock.pocketlifevehicle.command.new.builder.LiteralArgumentBuilder
import dev.sky_lock.pocketlifevehicle.command.new.builder.RequiredArgumentBuilder
import dev.sky_lock.pocketlifevehicle.command.new.node.RootCommandNode
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginCommand
import org.bukkit.craftbukkit.v1_14_R1.CraftServer
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

/**
 * @author sky_lock
 */

class PluginCommandExecutor : CommandExecutor {

    val plugin = VehiclePlugin.instance
    val root = RootCommandNode()

    fun register(command: ICommand) {
        this.register(command.builder)
    }

    fun register(builder: ArgumentBuilder) {
        if (builder !is LiteralArgumentBuilder) {
            throw IllegalStateException("Builder should not be NodeBuilder but LiteralNodeBuilder")
        }
        this.registerPluginCommand(builder.literal, builder.aliases, builder.description, builder.permission, this)

        val literalNode = builder.build()
        this.root.addChild(literalNode)

        /*if (CommodoreProvider.isSupported()) {
            val commodore = CommodoreProvider.getCommodore(this.plugin)

            val brigadierLiteralBuilder = com.mojang.brigadier.builder.LiteralArgumentBuilder.literal<Any>(name)
            val childNodes = literalBuilder.rootNode.getChildNodes()


            commodore.register(command, brigadierLiteralBuilder)
        }*/
    }

    private fun registerPluginCommand(name: String, aliases: List<String>, description: String, permission: String, executor: CommandExecutor) {
        val constructor = PluginCommand::class.java.getDeclaredConstructor(String::class.java, Plugin::class.java)
        constructor.isAccessible = true
        val command = constructor.newInstance(name, this.plugin)
        command.setExecutor(executor)
        command.description = description
        command.permission = permission
        command.aliases = aliases
        // TODO("permission message?")
        (Bukkit.getServer() as CraftServer).commandMap.register(this.plugin.description.name, command)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val commandNode = this.root.findChild(command.name.toLowerCase()) ?: TODO("when command not exists")
        var cursor = -1

        args.forEachIndexed { index, arg ->
            cursor++
            var node = commandNode
            if (index == 0) {
                sender.sendMessage(arg.toLowerCase())
                node = commandNode.findChild(arg.toLowerCase()) ?: TODO("when node is null, maybe help")
            }
            if (index == cursor) {
                for (i in 0..cursor + 1) {
                    if (args.size == index + 1) {
                        val exec = node.runnable ?: TODO("when command is null")
                        exec.run(sender, command, label, args)
                        return true
                    }
                    node = node.findChild(args[index + 1]) ?: TODO("when node is null")
                }
            }
        }

        if (cursor == -1) {
            val exec = commandNode.runnable ?: TODO("when command not exists")
            exec.run(sender, command, label, args)
        }
        return true
    }
}