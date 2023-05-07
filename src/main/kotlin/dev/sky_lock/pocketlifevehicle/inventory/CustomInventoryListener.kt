package dev.sky_lock.pocketlifevehicle.inventory

import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import dev.sky_lock.pocketlifevehicle.inventory.impl.ContainerModelTextEdit
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftInventory
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.server.PluginDisableEvent

/**
 * @author sky_lock
 */

class CustomInventoryListener : Listener {

    @EventHandler
    fun onInventoryOpen(event: InventoryOpenEvent) {
        val player = event.player as? Player ?: return
        val inventory = event.inventory
        if (inventory is InventoryCustom) inventory.onInventoryOpen(player)
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked
        val inventory = player.openInventory.topInventory
        if (inventory is InventoryCustom) {
            inventory.onInventoryClick(event)
            return
        }
        val craftInventory = inventory as CraftInventory
        if (craftInventory !is ContainerModelTextEdit.CraftModelTextEditor) {
            return
        }
        craftInventory.onClick(event)
    }

    @EventHandler
    fun onInventoryDrag(event: InventoryDragEvent) {
        val player = event.whoClicked
        val inventory = player.openInventory.topInventory
        if (inventory is InventoryCustom) inventory.onInventoryDrag(event)
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val player = event.player as? Player ?: return
        val inventory = event.inventory
        if (inventory is InventoryCustom) inventory.onInventoryClose(player)
        val craftInventory = inventory as CraftInventory
        if (craftInventory !is ContainerModelTextEdit.CraftModelTextEditor) {
            return
        }
        craftInventory.onClose(event)
    }

    @EventHandler
    fun onPluginDisable(event: PluginDisableEvent) {
        if (event.plugin !is VehiclePlugin) return
        for (player in event.plugin.server.onlinePlayers) {
            val inventory = player.openInventory.topInventory
            if (inventory is InventoryCustom) player.closeInventory()
        }
    }
}