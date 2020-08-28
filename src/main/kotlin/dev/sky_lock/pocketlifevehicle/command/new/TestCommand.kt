package dev.sky_lock.pocketlifevehicle.command.new

import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionDefault

/**
 * @author sky_lock
 */

class TestCommand : CommandBase() {

    override val permissionMessage = ChatColor.RED + "You don't have permission to perform this."
    override val builder =
        literal("test")
            .description("A test command")
            .alias("test2", "test3")
            .permission(Permission("test.root", "hogehogeeee", PermissionDefault.OP))
            .then(
                literal("golden")
                    .alias("gold")
                    .permission(Permission("hogehoge.hagehage.", "hogehogeeee", PermissionDefault.TRUE))
                    .executes { sender, cmd, label, args ->
                        val player = sender as Player
                        player.inventory.addItem(ItemStackBuilder(Material.GOLDEN_AXE, 1).build())
                        return@executes 1
                    }
                    .then(
                        argument("count", integer())
                            .then(
                                argument("text", string())
                                    .executes { sender, cmd, label, args ->

                                        return@executes 1
                                    }
                            )
                            .executes { sender, cmd, label, args ->
                                val player = sender as Player
                                player.inventory.addItem(ItemStackBuilder(Material.DIAMOND_AXE, 1).build())
                                return@executes 1
                            }
                    )
            )
            .then(
                literal("iron")
                    .alias("tetsu")
                    .permission(Permission("test.arg2", "hogehogeeee", PermissionDefault.OP))
                    .executes { sender, cmd, label, args ->
                        val player = sender as Player
                        player.inventory.addItem(ItemStackBuilder(Material.IRON_AXE, 1).build())
                        return@executes 1
                    }
            )
            .then(
                literal("playercomplete")
                    .alias("pc")
                    .then(
                        argument("player", player()).executes { sender, cmd, label, args ->
                            return@executes 1
                        }
                    )
            )
            .executes { sender, cmd, label, args ->
                sender.sendMessage("root command")
                return@executes 1
            }
}