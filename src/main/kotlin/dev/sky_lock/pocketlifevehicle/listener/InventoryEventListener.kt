package dev.sky_lock.pocketlifevehicle.listener

import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.gui.EditSessions
import dev.sky_lock.pocketlifevehicle.gui.ModelMenuIndex
import dev.sky_lock.pocketlifevehicle.gui.ModelOption
import dev.sky_lock.pocketlifevehicle.gui.StringEditor
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType

/**
 * @author sky_lock
 */
class InventoryEventListener : Listener {

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.slotType == InventoryType.SlotType.OUTSIDE) {
            return
        }
        val nmsItemStack = CraftItemStack.asNMSCopy(event.currentItem)
        if (nmsItemStack.hasTag() && nmsItemStack.tag!!.hasKey("editor-result")) {
            event.result = Event.Result.DENY
            event.isCancelled = true
            val result = event.currentItem
            val editor = StringEditor.get(event.whoClicked as Player)
            EditSessions.of(event.whoClicked.uniqueId).ifPresent { session: ModelOption ->
                val meta = result!!.itemMeta
                val displayName = meta.displayName
                when (editor?.editorType) {
                    StringEditor.Type.ID -> {
                        session.id = displayName
                    }
                    StringEditor.Type.NAME -> {
                        val name = ChatColor.translateAlternateColorCodes('&', displayName)
                        session.name = name
                    }
                    StringEditor.Type.HEIGHT -> {
                        val height = displayName.toFloatOrNull()
                        if (height == null) {
                            meta.lore = listOf(ChatColor.RED + "有効な数字を入力して下さい")
                            result.itemMeta = meta
                            return@ifPresent
                        }
                        session.height = height
                    }
                }
                val player = event.whoClicked as Player
                StringEditor.get(player)?.menu?.open(player, ModelMenuIndex.SETTING.ordinal)
                StringEditor.close(player)
            }
        }
        if (!StringEditor.isOpening(event.whoClicked as Player)) {
            return
        }
        if (event.clickedInventory != event.inventory) {
            return
        }
        event.result = Event.Result.DENY
        event.isCancelled = true
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val player = event.player as Player
        StringEditor.close(player)
        Bukkit.getScheduler().runTaskLater(VehiclePlugin.instance, Runnable {
            if (player.openInventory.topInventory.type == InventoryType.CRAFTING) {
                EditSessions.destroy(player.uniqueId)
            }
        }, 1L)
    }
}