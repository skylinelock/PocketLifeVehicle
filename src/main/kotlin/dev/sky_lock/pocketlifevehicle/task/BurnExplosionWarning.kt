package dev.sky_lock.pocketlifevehicle.task

import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import org.bukkit.ChatColor
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */
internal class BurnExplosionWarning : AbstractWarning() {
    fun send(player: Player) {
        super.send(player, ChatColor.GOLD + "乗り物は" + count + "秒後に爆発します")
    }
}