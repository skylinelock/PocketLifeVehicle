package dev.sky_lock.pocketlifevehicle.inventory

import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventoryCustom
import org.bukkit.entity.Player
import org.bukkit.event.inventory.*
import org.bukkit.inventory.ItemStack

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

    private fun setAction(index: Int, action: (InventoryClickEvent) -> Unit) {
        slots[index] = action
    }

    fun setSlot(index: Int, item: ItemStack, action: (InventoryClickEvent) -> Unit) {
        setItem(index, item)
        setAction(index, action)
    }

    fun addSelectGrowEffectToSingleItem(event: InventoryClickEvent) {
        val inventory = event.view.topInventory
        for (item in inventory.contents) {
            if (item == null) {
                continue
            }
            val meta = item.itemMeta
            if (meta.hasEnchant(ItemStackBuilder.GLOW_ENCHANT)) {
                meta.removeEnchant(ItemStackBuilder.GLOW_ENCHANT)
                item.itemMeta = meta
            }
        }
        val item = event.currentItem ?: return
        event.currentItem = ItemStackBuilder(item).addGlowEffect().build()
    }

    override fun clear(i: Int) {
        super.clear(i)
        slots.remove(i)
    }

    fun size(): Int {
        return slots.size
    }
}