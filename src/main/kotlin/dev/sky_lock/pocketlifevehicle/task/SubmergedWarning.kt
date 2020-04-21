package dev.sky_lock.pocketlifevehicle.task

import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import org.bukkit.ChatColor
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */
internal class SubmergedWarning : AbstractWarning() {
    fun send(player: Player) {
        super.send(player, ChatColor.GOLD + "乗り物は" + super.count + "秒後に水没します")
    }
}