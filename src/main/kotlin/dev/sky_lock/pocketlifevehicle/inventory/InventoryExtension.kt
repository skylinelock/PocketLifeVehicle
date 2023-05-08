package dev.sky_lock.pocketlifevehicle.inventory

import dev.sky_lock.pocketlifevehicle.inventory.impl.ContainerModelTextEdit
import dev.sky_lock.pocketlifevehicle.inventory.impl.InventoryEventVehicle
import dev.sky_lock.pocketlifevehicle.status.inventory.impl.InventoryVehicle
import dev.sky_lock.pocketlifevehicle.vehicle.EntityVehicle
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */

fun Player.openVehicleUtility(vehicle: EntityVehicle) {
    openInventory(InventoryVehicle(this, vehicle))
}

fun Player.openEventVehicleUtility(vehicle: EntityVehicle) {
    openInventory(InventoryEventVehicle(this, vehicle))
}

fun Player.openModelLoreEditor(default: String, model: Model) {
    ModelLoreEditor(this, model)
}

fun Player.openModelTextEditor(title: String, default: String, type: ContainerModelTextEdit.ModifyType, model: Model?) {
    openInventory(ContainerModelTextEdit(title, default, type, model, this).bukkitView)
}