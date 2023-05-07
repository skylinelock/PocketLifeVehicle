package dev.sky_lock.pocketlifevehicle.command.new

import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import dev.sky_lock.pocketlifevehicle.command.new.builder.ArgumentBuilder
import dev.sky_lock.pocketlifevehicle.command.new.builder.LiteralArgumentBuilder
import dev.sky_lock.pocketlifevehicle.command.new.node.RootCommandNode
import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.*
import org.bukkit.command.Command
import org.bukkit.craftbukkit.v1_19_R3.CraftServer
import org.bukkit.plugin.Plugin

/**
 * @author sky_lock
 */

class PluginCommandExecutor : CommandExecutor {

    val plugin = VehiclePlugin.instance
    val root = RootCommandNode()

    fun register(commandBase: CommandBase) {
        this.register(commandBase.builder, commandBase.permissionMessage)
    }

    fun register(builder: ArgumentBuilder, permissionMessage: String) {
        if (builder !is LiteralArgumentBuilder) {
            throw IllegalStateException("Builder should not be NodeBuilder but LiteralNodeBuilder")
        }
        this.registerPluginCommand(builder.literal, builder.aliases, builder.description, permissionMessage, this)

        val literalNode = builder.build()
        this.root.addChild(literalNode)

        /*if (CommodoreProvider.isSupported()) {
            val commodore = CommodoreProvider.getCommodore(this.plugin)

            val brigadierLiteralBuilder = com.mojang.brigadier.builder.LiteralArgumentBuilder.literal<Any>(name)
            val childNodes = literalBuilder.rootNode.getChildNodes()


            commodore.register(command, brigadierLiteralBuilder)
        }*/
    }

    private fun registerPluginCommand(name: String, aliases: List<String>, description: String, permissionMessage: String, executor: CommandExecutor) {
        val constructor = PluginCommand::class.java.getDeclaredConstructor(String::class.java, Plugin::class.java)
        constructor.isAccessible = true
        val command = constructor.newInstance(name, this.plugin)
        command.setExecutor(executor)
        command.aliases = aliases
        command.description = description
        command.permissionMessage = permissionMessage
        (Bukkit.getServer() as CraftServer).commandMap.register(this.plugin.description.name, command)
    }

    private fun sendPermissionMessage(sender: CommandSender, node: Command) {
        val permissionMessage = node.permissionMessage
            ?: ChatColor.RED + """I'm sorry, but you do not have permission to perform this command.
                    Please contact the server administrators if you believe that this is a mistake."""
        sender.sendMessage(permissionMessage)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        var node = this.root.findChild(command.name.lowercase())
            ?: throw CommandException("Illegal command name (bug)")

        args.forEach { arg ->
            node = node.findChild(arg.lowercase()) ?: return@forEach
        }
        val permission = node.permission

        if (permission == null) {
            if (!sender.isOp) {
                this.sendPermissionMessage(sender, command)
                return true
            }
        } else {
            if (!sender.hasPermission(permission)) {
                this.sendPermissionMessage(sender, command)
                return true
            }
        }

        val exec = node.runnable ?: throw CommandException("Illegal node (bug)")
        exec.run(sender, command, label, args)
        return true
    }
}