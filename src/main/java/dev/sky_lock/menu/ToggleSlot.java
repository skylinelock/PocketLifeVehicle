package dev.sky_lock.menu;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Consumer;

/**
 * @author sky_lock
 */

public class ToggleSlot extends Slot {
    private final ItemStack before;
    private final ItemStack after;
    private final Consumer<InventoryClickEvent> beforeClick;
    private final Consumer<InventoryClickEvent> afterClick;
    private ItemStack current;

    public ToggleSlot(int index, boolean beforeStart, ItemStack before, ItemStack after, Consumer<InventoryClickEvent> beforeClick, Consumer<InventoryClickEvent> afterClick) {
        super(index, before, beforeClick);

        if (beforeStart) {
            this.current = before;
        } else {
            this.current = after;
        }
        this.before = before;
        this.after = after;
        this.beforeClick = beforeClick;
        this.afterClick = afterClick;
    }

    @Override
    public ItemStack getItemStack() {
        return current;
    }

    @Override
    public void click(InventoryClickEvent event) {
        if (this.current.equals(before)) {
            beforeClick.accept(event);
            event.setCurrentItem(after);
            this.current = after;
        } else if (this.current.equals(after)) {
            afterClick.accept(event);
            event.setCurrentItem(before);
            this.current = before;
        }
    }
}
