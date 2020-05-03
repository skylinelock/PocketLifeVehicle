package dev.sky_lock.pocketlifevehicle.task

import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import org.bukkit.ChatColor
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */
open class AbstractWarning {
    var count = 0

    fun send(player: Player, subTitle: String) {
        player.sendTitle(WARNING, subTitle, 1, 20, 1)
    }

    fun stop(player: Player) {
        player.resetTitle()
    }

    companion object {
        private val WARNING = ChatColor.RED + "⚠⚠WARNING⚠⚠"
    }
}