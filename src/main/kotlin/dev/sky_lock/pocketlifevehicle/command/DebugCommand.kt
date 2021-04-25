package dev.sky_lock.pocketlifevehicle.command

import dev.sky_lock.pocketlifevehicle.inventory.impl.InventoryListModel
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */
class DebugCommand : ICommand, IAdminCommand {
    override fun execute(sender: CommandSender, cmd: Command, args: Array<String>) {
        val player = sender as Player
        player.openInventory(InventoryListModel(player))
    }
}