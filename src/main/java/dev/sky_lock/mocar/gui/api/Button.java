package dev.sky_lock.mocar.gui.api;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * @author sky_lock
 */

public class Button implements IGuiComponent {

    private ItemStack itemStack;
    private final int slot;
    private final Consumer<InventoryClickEvent> consumer;

    public Button(int slot, ItemStack itemStack, Consumer<InventoryClickEvent> consumer) {
        this.itemStack = itemStack;
        this.slot = slot;
        this.consumer = consumer;
    }

    public int getSlot() {
        return slot;
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        consumer.accept(event);
    }

    @Override
    public SlotRange getSlotRange() {
        return new SlotRange(slot);
    }
}
