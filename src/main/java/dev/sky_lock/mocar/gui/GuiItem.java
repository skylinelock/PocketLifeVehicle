package dev.sky_lock.mocar.gui;

import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * @author sky_lock
 */

public class GuiItem implements IGuiComponent {

    private final ItemStack itemStack;
    private final int slot;
    private final Consumer<InventoryClickEvent> consumer;

    public GuiItem(int slot, ItemStack itemStack, Consumer<InventoryClickEvent> consumer) {
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
        event.setResult(Event.Result.DENY);
        consumer.accept(event);
    }

    @Override
    public SlotRange getSlotRange() {
        return new SlotRange(slot);
    }
}
