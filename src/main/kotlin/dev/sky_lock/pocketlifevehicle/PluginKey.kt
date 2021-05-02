package dev.sky_lock.pocketlifevehicle

import org.bukkit.NamespacedKey

/**
 * @author sky_lock
 */

object PluginKey {
    private val plugin = VehiclePlugin.instance
    val OWNER = NamespacedKey(plugin, "owner")
    val FUEL = NamespacedKey(plugin, "fuel")
    val ID = NamespacedKey(plugin, "id")
}