package dev.sky_lock.pocketlifevehicle.vehicle.model

import org.bukkit.inventory.EquipmentSlot

/**
 * @author sky_lock
 */
enum class ItemPosition(val label: String, val slot: EquipmentSlot) {
    HEAD("頭", EquipmentSlot.HEAD),
    HAND("手", EquipmentSlot.HAND),
    CHEST("胸", EquipmentSlot.CHEST),
    FEET("足", EquipmentSlot.FEET),
    LEGS("膝", EquipmentSlot.LEGS);
}