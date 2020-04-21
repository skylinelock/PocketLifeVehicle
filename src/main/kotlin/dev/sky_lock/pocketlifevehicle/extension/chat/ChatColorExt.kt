package dev.sky_lock.pocketlifevehicle.extension.chat

import org.bukkit.ChatColor

/**s
 * @author sky_lock
 */

operator fun ChatColor.plus(text: String): String = this.toString() + text

operator fun ChatColor.plus(color: ChatColor): String = this.toString() + color.toString()