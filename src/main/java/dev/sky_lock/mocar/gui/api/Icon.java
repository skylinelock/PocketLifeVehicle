package dev.sky_lock.mocar.gui.api;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author sky_lock
 */

public class Icon implements IGuiComponent {
    private final SlotRange range;
    private final ItemStack itemStack;

    public Icon(int slot, ItemStack itemStack) {
        this.range = new SlotRange(slot);
        this.itemStack = itemStack;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        ;
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public SlotRange getSlotRange() {
        return range;
    }
}
