package dev.sky_lock.mocar.gui.api;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * @author sky_lock
 */

public class ToggleButton implements IGuiComponent {
    private final int slot;
    private ItemStack current;
    private final ItemStack before;
    private final ItemStack after;
    private Consumer<InventoryClickEvent> beforeClick;
    private Consumer<InventoryClickEvent> afterClick;

    public ToggleButton(int slot, boolean order, ItemStack before, ItemStack after, Consumer<InventoryClickEvent> beforeClick, Consumer<InventoryClickEvent> afterClick) {
        this.slot = slot;
        if (order) {
            current = before;
        } else {
            current = after;
        }
        this.before = before;
        this.after = after;
        this.beforeClick = beforeClick;
        this.afterClick = afterClick;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if (event.getCurrentItem().equals(before)) {
            beforeClick.accept(event);
            event.getInventory().setItem(slot, after);
        } else {
            afterClick.accept(event);
            event.getInventory().setItem(slot, before);
        }
    }

    @Override
    public ItemStack getItemStack() {
        return current;
    }

    @Override
    public SlotRange getSlotRange() {
        return new SlotRange(slot);
    }
}
