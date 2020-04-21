package dev.sky_lock.pocketlifevehicle

import org.bukkit.permissions.Permissible

/**
 * @author sky_lock
 */
 
 enum class Permission(private val permission: String) {
    ADMIN_COMMAND("plvehicle.command.admin.use"),
    VEHICLE_OPEN_GUI("plvehicle.entity.open-gui"),
    VEHICLE_PLACE("plvehicle.entity.place");

    fun obtained(permissible: Permissible): Boolean {
        return permissible.hasPermission(permission)
    }
}