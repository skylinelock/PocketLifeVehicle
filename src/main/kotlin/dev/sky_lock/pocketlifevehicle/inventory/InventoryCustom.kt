package dev.sky_lock.pocketlifevehicle.inventory

import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftInventoryCustom
import org.bukkit.entity.Player
import org.bukkit.event.inventory.*
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * @author sky_lock
 */

abstract class InventoryCustom(size: Int, title: String) : CraftInventoryCustom(null, size, title) {

    private val slots = mutableMapOf<Int, (InventoryClickEvent) -> Unit>()

    open fun onInventoryOpen(player: Player) {}

    open fun onInventoryClick(event: InventoryClickEvent) {
        if (event.slotType == InventoryType.SlotType.OUTSIDE) {
            return
        }
        if (event.click.isShiftClick) {
            event.isCancelled = true
            return
        }
        // クリックしたインベントリーがBottomInventoryか
        if (event.clickedInventory == event.view.bottomInventory) {
            return
        }
        // クリックしたインベントリーがTopInventoryか
        if (event.inventory != event.clickedInventory) {
            return
        }
        if (event.action == InventoryAction.HOTBAR_SWAP || event.action == InventoryAction.HOTBAR_MOVE_AND_READD) {
            event.isCancelled = true
            return
        }
        if (!(event.click.isRightClick || event.click.isLeftClick)) {
            event.isCancelled = true
            return
        }
        event.isCancelled = true
        slots.asSequence().find { slot -> slot.key == event.slot }?.value?.invoke(event)
    }

    open fun onInventoryDrag(event: InventoryDragEvent) {
        if (event.inventory == event.view.bottomInventory) {
            if (event.type == DragType.SINGLE) {
                return
            }
        }
        event.isCancelled = true
    }

    open fun onInventoryClose(player: Player) {}

    open fun onUpdate() {}

    private fun setAction(index: Int, action: (InventoryClickEvent) -> Unit) {
        slots[index] = action
    }

    fun setSlot(index: Int, item: ItemStack, action: (InventoryClickEvent) -> Unit) {
        setItem(index, item)
        setAction(index, action)
    }

    override fun clear(i: Int) {
        super.clear(i)
        slots.remove(i)
    }

    fun size(): Int {
        return slots.size
    }
}