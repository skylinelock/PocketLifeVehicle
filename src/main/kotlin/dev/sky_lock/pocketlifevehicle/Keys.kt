package dev.sky_lock.pocketlifevehicle

import org.bukkit.NamespacedKey

/**
 * @author sky_lock
 */

enum class Keys(val label: String) {
    MODEL_ID("Vehicle.ModelID"),
    OWNER("Vehicle.Owner"),
    FUEL("Vehicle.Fuel"),
    ID("Vehicle.Id");

    fun namespace(): NamespacedKey {
        return NamespacedKey(VehiclePlugin.instance, label)
    }
}