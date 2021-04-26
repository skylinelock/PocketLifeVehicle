package dev.sky_lock.pocketlifevehicle.inventory

import dev.sky_lock.pocketlifevehicle.inventory.impl.InventoryVehicle
import dev.sky_lock.pocketlifevehicle.vehicle.Vehicle
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftInventoryPlayer
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */
 
 fun Player.openTextEditInventory(title: String, default: String) {
    val entityPlayer = (this as CraftPlayer).handle
    val inventory = (this.inventory as CraftInventoryPlayer).inventory
    val world = (this.world as CraftWorld).handle
    openInventory(
        ContainerTextEdit(
            title, entityPlayer.nextContainerCounter(), inventory, world, default
        ).bukkitView
    )
 }

fun Player.openVehicleUtilityMenu(vehicle: Vehicle) {
    openInventory(InventoryVehicle(this, vehicle))
}