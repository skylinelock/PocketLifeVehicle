package dev.sky_lock.pocketlifevehicle.task

import dev.sky_lock.pocketlifevehicle.extension.chat.Line
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */
class SubmergedWarning : AbstractWarning() {
    fun send(player: Player) {
        super.send(player, Line().gold("乗り物は" + super.count + "秒後に水没します"))
    }
}