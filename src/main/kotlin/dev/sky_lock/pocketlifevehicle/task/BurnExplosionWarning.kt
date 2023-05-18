package dev.sky_lock.pocketlifevehicle.task

import dev.sky_lock.pocketlifevehicle.text.Line
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */
class BurnExplosionWarning : AbstractWarning() {
    fun send(player: Player) {
        super.send(player, Line().gold("乗り物は" + count + "秒後に爆発します"))
    }
}