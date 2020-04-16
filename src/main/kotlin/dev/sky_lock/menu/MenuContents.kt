package dev.sky_lock.menu

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * @author sky_lock
 */
abstract class MenuContents {
    private val slots: MutableList<Slot> = ArrayList()
    fun close() {
        slots.clear()
    }

    fun addSlot(vararg slots: Slot) {
        Collections.addAll(this.slots, *slots)
    }

    fun updateItemStack(index: Int, newStack: ItemStack) {
        slots.stream().filter { slot: Slot -> slot.index() == index }.findFirst().ifPresent { slot: Slot -> slot.setItemStack(newStack) }
    }

    fun getSlots(): List<Slot> {
        return slots
    }

    fun size(): Int {
        return slots.size
    }

    fun removeSlot(index: Int) {
        slots.stream().filter { slot: Slot -> slot.index() == index }.findFirst().ifPresent { o: Slot -> slots.remove(o) }
    }

    fun click(event: InventoryClickEvent) {
        slots.stream().filter { slot: Slot -> slot.index() == event.slot }.findFirst().ifPresent { slot: Slot -> slot.click(event) }
    }

    abstract fun onFlip(menu: InventoryMenu)
}