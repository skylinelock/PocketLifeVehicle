package dev.sky_lock.pocketlifevehicle.command

import com.mojang.brigadier.arguments.ArgumentType
import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import dev.sky_lock.pocketlifevehicle.command.lib.PlayerArgumentType
import dev.sky_lock.pocketlifevehicle.command.lib.builder.ArgumentBuilder
import dev.sky_lock.pocketlifevehicle.command.lib.builder.LiteralArgumentBuilder
import dev.sky_lock.pocketlifevehicle.command.lib.builder.RequiredArgumentBuilder
import dev.sky_lock.pocketlifevehicle.command.lib.node.RootCommandNode
import me.lucko.commodore.CommodoreProvider
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

/**
 * @author sky_lock
 */

class PluginCommandExecutor : CommandExecutor {

    val plugin = VehiclePlugin.instance
    val root = RootCommandNode()

    init {
        this.register(
            literal("test")
                .then(
                    literal("test").executes { sender, cmd, label, args ->
                        return@executes 1
                    }
                )
                .then(
                    literal("test2").then(
                        argument("player", player()).executes { sender, cmd, label, args ->
                            return@executes 1
                        }
                    )

                )
                .executes { sender, cmd, label, args ->
                    sender.sendMessage("specify sub command")
                    return@executes 1
                }
        )
    }

    private fun literal(literal: String): LiteralArgumentBuilder {
        return LiteralArgumentBuilder(literal)
    }

    private fun <T> argument(name: String, type: ArgumentType<T>): RequiredArgumentBuilder<T> {
        return RequiredArgumentBuilder(name, type)
    }

    private fun player(): PlayerArgumentType {
        return PlayerArgumentType.player()
    }

    private fun register(builder: ArgumentBuilder) {
        if (builder !is LiteralArgumentBuilder) {
            throw IllegalStateException("Builder should not be NodeBuilder but LiteralNodeBuilder")
        }
        val literalBuilder = builder as LiteralArgumentBuilder
        val name = literalBuilder.literal
        val command = this.plugin.getCommand(name) ?: throw IllegalStateException("Command '$name' not in plugin.yml")
        command.setExecutor(this)

        val literalNode = literalBuilder.build()
        this.root.addChild(literalNode)

        /*if (CommodoreProvider.isSupported()) {
            val commodore = CommodoreProvider.getCommodore(this.plugin)

            val brigadierLiteralBuilder = com.mojang.brigadier.builder.LiteralArgumentBuilder.literal<Any>(name)
            literalNode.getChildNodes().forEach { node ->

            }

            commodore.register(command, brigadierLiteralBuilder)
        }*/
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val commandNode = this.root.findChild(command.name.toLowerCase()) ?: TODO("when command not exists")
        var cursor = -1

        args.forEachIndexed { index, arg ->
            if (index == cursor) {
                var node = commandNode
                for (i in 0..cursor) {
                    val current = node.findChild(arg.toLowerCase())
                    if (current == null) {
                        val exec = node.command ?: TODO("when command is null")
                        exec.run(sender, command, label, args)
                        return true
                    }
                    node = current
                }
            } else {
                cursor++
            }
        }

        if (cursor == -1) {
            TODO("when args is empty")
        }
        return true
    }
}