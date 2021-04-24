package dev.sky_lock.menu

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

/**
 * @author sky_lock
 */
class ToggleSlot(index: Int, beforeStart: Boolean, before: ItemStack, after: ItemStack, beforeClick: (InventoryClickEvent) -> Unit, afterClick: (InventoryClickEvent) -> Unit) : Slot(index, before, beforeClick) {
    private val before: ItemStack
    private val after: ItemStack
    private val beforeClick: (InventoryClickEvent) -> Unit
    private val afterClick: (InventoryClickEvent) -> Unit
    private var current: ItemStack? = null
    override var itemStack: ItemStack = before
        get() = current!!

    override fun click(event: InventoryClickEvent) {
        if (current == before) {
            beforeClick(event)
            event.currentItem = after
            current = after
        } else if (current == after) {
            afterClick(event)
            event.currentItem = before
            current = before
        }
    }

    init {
        current = if (beforeStart) before else after
        this.before = before
        this.after = after
        this.beforeClick = beforeClick
        this.afterClick = afterClick
    }
}