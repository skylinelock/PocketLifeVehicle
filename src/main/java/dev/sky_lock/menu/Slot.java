package dev.sky_lock.menu;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Consumer;

/**
 * @author sky_lock
 */

public class Slot {
    private final int index;
    private final Consumer<InventoryClickEvent> todo;
    private ItemStack itemStack;

    public Slot(int index, ItemStack itemStack, Consumer<InventoryClickEvent> todo) {
        this.index = index;
        this.itemStack = itemStack;
        this.todo = todo;
    }

    /**
     * インベントリでのスロットナンバーを返します。
     *
     * @return スロットナンバー
     */
    public int index() {
        return index;
    }

    /**
     * このスロットのアイテムを返します。
     *
     * @return インベントリに表示されるアイテム
     */
    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * このスロットがクリックされたと時に呼ばれる処理です。
     *
     * @param event InventoryClickEvent
     */
    public void click(InventoryClickEvent event) {
        todo.accept(event);
    }
}
