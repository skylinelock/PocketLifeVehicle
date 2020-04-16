package dev.sky_lock.pocketlifevehicle.task

import org.bukkit.ChatColor
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */
internal class BurnExplosionWarning : AbstractWarning() {
    fun send(player: Player) {
        super.send(player, ChatColor.GOLD.toString() + "乗り物は" + count + "秒後に爆発します")
    }
}