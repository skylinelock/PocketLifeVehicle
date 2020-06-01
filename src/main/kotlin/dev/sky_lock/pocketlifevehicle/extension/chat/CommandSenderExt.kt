package dev.sky_lock.pocketlifevehicle.extension.chat

import dev.sky_lock.pocketlifevehicle.Permission
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

/**
 * @author sky_lock
 */

fun CommandSender.sendVehiclePrefixedMessage(message: String) {
    val prefix = ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "Vehicle" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET
    if (Permission.ADMIN_COMMAND.obtained(this)) {
        this.sendMessage(prefix + ChatColor.RESET + message)
    } else {
        this.sendMessage(message)
    }
}

fun CommandSender.sendRacePrefixedMessage(message: String) {
    val prefix = ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "Race" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET
    this.sendMessage(prefix + ChatColor.RESET + message)
}