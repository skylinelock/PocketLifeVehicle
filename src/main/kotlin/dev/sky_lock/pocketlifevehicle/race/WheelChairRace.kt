package dev.sky_lock.pocketlifevehicle.race

import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author sky_lock
 */

class WheelChairRace {
    val participants: MutableList<UUID> = ArrayList()

    fun join(player: Player) {
        if (!participants.contains(player.uniqueId)) {
            participants.add(player.uniqueId)
        }
    }

    fun leave(player: Player) {
        participants.remove(player.uniqueId)
    }

    fun start() {

    }
}