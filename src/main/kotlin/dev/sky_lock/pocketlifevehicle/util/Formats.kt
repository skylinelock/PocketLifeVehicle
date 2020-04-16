package dev.sky_lock.pocketlifevehicle.util

import org.bukkit.ChatColor

/**
 * @author sky_lock
 */
object Formats {
    fun truncateToOneDecimalPlace(f: Float): String {
        return String.format("%.1f", f)
    }

    fun removeBlanks(blankText: String): String {
        return blankText.replace("\\s".toRegex(), "")
    }

    fun colorize(text: String): String {
        return ChatColor.translateAlternateColorCodes('&', text)
    }
}