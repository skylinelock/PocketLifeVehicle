package dev.sky_lock.mocar.gui.api;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author sky_lock
 */

public class Panel implements IGuiComponent {
    private final ItemStack itemStack;
    private final SlotRange range;
    private final Consumer<InventoryClickEvent> consumer;

    public Panel(List<Integer> slots, ItemStack itemStack, Consumer<InventoryClickEvent> consumer) {
        range = new SlotRange(slots);
        this.consumer = consumer;
        this.itemStack = itemStack;
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
        return range;
    }
}
