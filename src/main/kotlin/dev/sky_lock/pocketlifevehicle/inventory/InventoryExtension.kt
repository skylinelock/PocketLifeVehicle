package dev.sky_lock.pocketlifevehicle.inventory

import dev.sky_lock.pocketlifevehicle.inventory.impl.ContainerModelTextEdit
import dev.sky_lock.pocketlifevehicle.inventory.impl.InventoryVehicle
import dev.sky_lock.pocketlifevehicle.vehicle.Vehicle
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */

fun Player.openVehicleUtilityMenu(vehicle: Vehicle) {
    openInventory(InventoryVehicle(this, vehicle))
}

fun Player.openModelLoreEditor(default: String, model: Model) {
    ModelLoreEditor(this, model)
}

fun Player.openModelTextEditor(title: String, default: String, model: Model?) {
    openInventory(ContainerModelTextEdit(title, default, model, this).bukkitView)
}