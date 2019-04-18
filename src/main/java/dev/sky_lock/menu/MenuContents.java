package dev.sky_lock.menu;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author sky_lock
 */

public abstract class MenuContents {
    private final List<Slot> slots = new ArrayList<>();

    public void close() {
        slots.clear();
    }

    public void addSlot(Slot... slots) {
        Collections.addAll(this.slots, slots);
    }

    public void updateItemStack(int index, ItemStack newStack) {
        slots.stream().filter(slot -> slot.index() == index).findFirst().ifPresent(slot -> slot.setItemStack(newStack));
    }

    public List<Slot> getSlots() {
        return slots;
    }

    public int size() {
        return slots.size();
    }

    public void clear() {
        slots.clear();
    }

    void click(InventoryClickEvent event) {
        slots.stream().filter(slot -> slot.index() == event.getSlot()).findFirst().ifPresent(slot -> slot.click(event));
    }

    public abstract void onFlip(InventoryMenu menu);


}
