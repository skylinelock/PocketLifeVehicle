package dev.sky_lock.menu

import dev.sky_lock.menu.InventoryMenu.Companion.of
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.*
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

/**
 * @author sky_lock
 */
class InventoryMenuListener(private val plugin: JavaPlugin) : Listener {
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val holder = (event.inventory.holder ?: return) as? InventoryMenu ?: return
        if (event.slotType == InventoryType.SlotType.OUTSIDE) {
            return
        }
        if (event.clickedInventory == event.view.bottomInventory) {
            if (event.click.isShiftClick) {
                event.isCancelled = true
            }
            return
        }
        if (event.inventory != event.clickedInventory) {
            return
        }
        if (event.action == InventoryAction.HOTBAR_SWAP || event.action == InventoryAction.HOTBAR_MOVE_AND_READD) {
            event.isCancelled = true
            return
        }
        if (!event.click.isRightClick && !event.click.isLeftClick) {
            event.isCancelled = true
            return
        }
        event.isCancelled = true
        holder.click(event)
    }

    @EventHandler
    fun onInventoryDrag(event: InventoryDragEvent) {
        of((event.whoClicked as Player)).ifPresent { menu: InventoryMenu? ->
            if (event.inventory == event.view.bottomInventory) {
                if (event.type == DragType.SINGLE) {
                    return@ifPresent
                }
            }
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val menu = of((event.player as Player))
        object : BukkitRunnable() {
            override fun run() {
                menu.ifPresent { menu: InventoryMenu -> menu.remove((event.player as Player)) }
            }
        }.runTaskLater(plugin, 1L)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        of(event.player).ifPresent { menu: InventoryMenu -> menu.close(event.player) }
    }

}