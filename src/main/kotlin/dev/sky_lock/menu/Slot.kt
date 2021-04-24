package dev.sky_lock.menu

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

/**
 * @author sky_lock
 */
open class Slot(val index: Int, open var itemStack: ItemStack, private val onclick: (InventoryClickEvent) -> Unit) {

    constructor(index: Int, itemStack: ItemStack) : this(index, itemStack, {})

    open fun click(event: InventoryClickEvent) {
        onclick(event)
    }

}