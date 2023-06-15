package dev.sky_lock.pocketlifevehicle.util

import dev.sky_lock.pocketlifevehicle.text.Line
import org.bukkit.Bukkit

/**
 * @author sky_lock
 */

object DevUtil {

    fun logInfo(text: String) {
        Bukkit.getLogger().info(text)
    }

    fun broadcast(text: String) {
        Line().raw(text).broadcast()
    }
}