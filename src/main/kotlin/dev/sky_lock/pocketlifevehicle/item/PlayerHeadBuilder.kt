package dev.sky_lock.pocketlifevehicle.item

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

/**
 * @author sky_lock
 */
class PlayerHeadBuilder constructor(amount: Int): ItemStackBuilder(Material.PLAYER_HEAD, amount) {

    fun owingPlayer(uuid: UUID?): PlayerHeadBuilder {
        if (uuid != null) {
            val player = Bukkit.getOfflinePlayer(uuid)
            (itemMeta as SkullMeta).owningPlayer = player
        }
        return this
    }
}