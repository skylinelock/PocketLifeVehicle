package dev.sky_lock.pocketlifevehicle

import org.bukkit.permissions.Permissible

/**
 * @author sky_lock
 */

 enum class Permission(private val permission: String) {
    ADMIN_COMMAND("plvehicle.command.admin.use"),
    OPEN_VEHICLE_GUI("plvehicle.entity.gui.open"),
    OPEN_EVENT_VEHICLE_GUI("plvehicle.entity.event.gui.open"),
    PLACE_OTHER_VEHICLE("plvehicle.entity.place.other");

    fun obtained(permissible: Permissible): Boolean {
        return permissible.hasPermission(permission)
    }
}