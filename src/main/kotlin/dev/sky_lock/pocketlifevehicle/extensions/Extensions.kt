package dev.sky_lock.pocketlifevehicle.extensions

import dev.sky_lock.pocketlifevehicle.Permission
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

/**
 * @author sky_lock
 */

fun CommandSender.sendPrefixedPluginMessage(message: String) {
    val prefix = ChatColor.DARK_GRAY.toString() + "[" + ChatColor.DARK_GREEN.toString() + "PLVehicle" + ChatColor.DARK_GRAY.toString() + "] " + ChatColor.RESET.toString()
    if (Permission.ADMIN_COMMAND.obtained(this)) {
        this.sendMessage(prefix + ChatColor.RESET + message)
    } else {
        this.sendMessage(message)
    }
}

operator fun ChatColor.plus(text: String): String {
    return this.toString() + text
}