package dev.sky_lock.pocketlifevehicle.command.new

import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */

class TestCommand : ICommand {

    val executor = VehiclePlugin.instance.commandExecutor
    override val builder =
        literal("test")
            .description("A test command")
            .alias("test2", "test3")
            .permission("test.perm")
            .then(
                literal("golden")
                    .alias("gold")
                    .executes { sender, cmd, label, args ->
                    val player = sender as Player
                    player.inventory.addItem(ItemStackBuilder(Material.GOLDEN_AXE, 1).build())
                    return@executes 1
                }
                    .then(
                        argument("count", integer()).executes { sender, cmd, label, args ->
                            val player = sender as Player
                            player.inventory.addItem(ItemStackBuilder(Material.DIAMOND_AXE, 1).build())
                            return@executes 1
                        }
                    )
            )
            .then(
                literal("iron")
                    .alias("tetsu")
                    .executes { sender, cmd, label, args ->
                    val player = sender as Player
                    player.inventory.addItem(ItemStackBuilder(Material.IRON_AXE, 1).build())
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
}