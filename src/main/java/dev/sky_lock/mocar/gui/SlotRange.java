package dev.sky_lock.mocar.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author sky_lock
 */

public class SlotRange {
    private final List<Integer> slots = new ArrayList<>();

    public SlotRange(int start, int end) {
        for (int i = 0; i < end - start + 1; i++) {
            slots.add(start + i);
        }
    }

    public SlotRange(int slot) {
        this(slot, slot);
    }

    public SlotRange(List<Integer> slots) {
        this.slots.addAll(slots);
    }

    public boolean contains(int slot) {
        return slots.contains(slot);
    }

    public boolean confilct(IGuiComponent component) {
        return slots.stream().anyMatch(slot -> component.getSlotRange().contains(slot));
    }

    public void forEach(Consumer<Integer> consumer) {
        slots.forEach(consumer);
    }
}
