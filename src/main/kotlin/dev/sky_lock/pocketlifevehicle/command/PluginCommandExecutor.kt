package dev.sky_lock.pocketlifevehicle.command

import com.mojang.brigadier.arguments.ArgumentType
import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import dev.sky_lock.pocketlifevehicle.command.lib.PlayerArgumentType
import dev.sky_lock.pocketlifevehicle.command.lib.builder.ArgumentBuilder
import dev.sky_lock.pocketlifevehicle.command.lib.builder.LiteralArgumentBuilder
import dev.sky_lock.pocketlifevehicle.command.lib.builder.RequiredArgumentBuilder
import dev.sky_lock.pocketlifevehicle.command.lib.node.RootCommandNode
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

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
                    literal("normal").executes { sender, cmd, label, args ->
                        val player = sender as Player
                        player.inventory.addItem(ItemStackBuilder(Material.GOLDEN_AXE, 1).build())
                        return@executes 1
                    }
                )
                .then(
                    literal("complete").then(
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
            val childNodes = literalBuilder.rootNode.getChildNodes()


            commodore.register(command, brigadierLiteralBuilder)
        }*/
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val commandNode = this.root.findChild(command.name.toLowerCase()) ?: TODO("when command not exists")
        var cursor = -1

        args.forEachIndexed { index, arg ->
            cursor++
            var node = commandNode
            if (index == 0) {
                node = commandNode.findChild(arg.toLowerCase()) ?: TODO("when node is null, maybe help")
            }
            if (index == cursor) {
                for (i in 0..cursor+1) {
                    if (args.size == index + 1) {
                        val exec = node.command ?: TODO("when command is null")
                        exec.run(sender, command, label, args)
                        return true
                    }
                    node = node.findChild(args[index + 1]) ?: TODO("when node is null")
                }
            }
        }

        if (cursor == -1) {
            val exec = commandNode.command ?: TODO("when command not exists")
            exec.run(sender, command, label, args)
        }
        return true
    }
}