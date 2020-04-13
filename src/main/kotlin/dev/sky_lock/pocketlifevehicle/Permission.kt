package dev.sky_lock.pocketlifevehicle

import org.bukkit.permissions.Permissible

/**
 * @author sky_lock
 */
 
 enum class Permission(val permission: String) {
    ADMIN_COMMAND("plvehicle.command.admin.use"),
    CAR_CLICK("plvehicle.entity.open-gui"),
    CAR_PLACE("plvehicle.entity.place");

    fun obtained(permissible: Permissible): Boolean {
        return permissible.hasPermission(permission)
    }
}