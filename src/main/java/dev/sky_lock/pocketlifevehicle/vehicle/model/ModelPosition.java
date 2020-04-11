package dev.sky_lock.pocketlifevehicle.vehicle.model;

import org.bukkit.inventory.EquipmentSlot;

/**
 * @author sky_lock
 */

public enum ModelPosition {
    HEAD("頭", EquipmentSlot.HEAD),
    HAND("手", EquipmentSlot.HAND),
    CHEST("胸", EquipmentSlot.CHEST),
    FEET("膝", EquipmentSlot.FEET),
    LEGS("足", EquipmentSlot.OFF_HAND);

    private final String label;
    private final EquipmentSlot slot;

    ModelPosition(String label, EquipmentSlot slot) {
        this.label = label;
        this.slot = slot;
    }

    public String getLabel() {
        return this.label;
    }

    public EquipmentSlot getSlot() {
        return this.slot;
    }
}
