package dev.sky_lock.menu

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Consumer

/**
 * @author sky_lock
 */
class ToggleSlot(index: Int, beforeStart: Boolean, before: ItemStack, after: ItemStack, beforeClick: Consumer<InventoryClickEvent>, afterClick: Consumer<InventoryClickEvent>) : Slot(index, before, beforeClick) {
    private val before: ItemStack
    private val after: ItemStack
    private val beforeClick: Consumer<InventoryClickEvent>
    private val afterClick: Consumer<InventoryClickEvent>
    private var current: ItemStack? = null
    override fun getItemStack(): ItemStack {
        return current!!
    }

    override fun click(event: InventoryClickEvent) {
        if (current == before) {
            beforeClick.accept(event)
            event.currentItem = after
            current = after
        } else if (current == after) {
            afterClick.accept(event)
            event.currentItem = before
            current = before
        }
    }

    init {
        if (beforeStart) {
            current = before
        } else {
            current = after
        }
        this.before = before
        this.after = after
        this.beforeClick = beforeClick
        this.afterClick = afterClick
    }
}