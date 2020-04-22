package dev.sky_lock.pocketlifevehicle.gui

import dev.sky_lock.menu.InventoryMenu
import dev.sky_lock.pocketlifevehicle.gui.contents.CarUtilContents
import dev.sky_lock.pocketlifevehicle.vehicle.Vehicle
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */
class CarUtilMenu(holder: Player, vehicle: Vehicle) : InventoryMenu("ユーティリティ", Type.BIG, holder) {
    init {
        addContents(CarUtilContents(vehicle))
    }
}