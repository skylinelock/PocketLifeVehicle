package dev.sky_lock.mocar.gui.api;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author sky_lock
 */

public class Gage implements IGuiComponent {
    private final SlotRange range;
    private final ItemStack empty;
    private final ItemStack full;

    public Gage(int start, int end, ItemStack empty, ItemStack full) {
        range = new SlotRange(start, end);
        this.empty = empty;
        this.full = full;
    }

    @Override
    public void onClick(InventoryClickEvent event) {

    }

    @Override
    public ItemStack getItemStack() {
        return empty;
    }

    @Override
    public SlotRange getSlotRange() {
        return range;
    }
}
