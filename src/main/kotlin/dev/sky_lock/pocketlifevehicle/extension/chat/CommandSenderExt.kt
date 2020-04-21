package dev.sky_lock.pocketlifevehicle.extension.chat

import dev.sky_lock.pocketlifevehicle.Permission
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

/**
 * @author sky_lock
 */

fun CommandSender.sendPrefixedPluginMessage(message: String) {
    val prefix = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "PLVehicle" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET
    if (Permission.ADMIN_COMMAND.obtained(this)) {
        this.sendMessage(prefix + ChatColor.RESET + message)
    } else {
        this.sendMessage(message)
    }
}