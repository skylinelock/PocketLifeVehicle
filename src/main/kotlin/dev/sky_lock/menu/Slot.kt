package dev.sky_lock.menu

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.function.Consumer

/**
 * @author sky_lock
 */
open class Slot(private val index: Int, private var itemStack: ItemStack, private val todo: (InventoryClickEvent) -> Unit) {

    constructor(index: Int, itemStack: ItemStack) : this(index, itemStack, {})

    /**
     * インベントリでのスロットナンバーを返します。
     *
     * @return スロットナンバー
     */
    fun index(): Int {
        return index
    }

    /**
     * このスロットのアイテムを返します。
     *
     * @return インベントリに表示されるアイテム
     */
    open fun getItemStack(): ItemStack {
        return itemStack
    }

    fun setItemStack(itemStack: ItemStack) {
        this.itemStack = itemStack
    }

    /**
     * このスロットがクリックされたと時に呼ばれる処理です。
     *
     * @param event InventoryClickEvent
     */
    open fun click(event: InventoryClickEvent) {
        todo(event)
    }

}