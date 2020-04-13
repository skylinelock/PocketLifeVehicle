package dev.sky_lock.pocketlifevehicle.item

import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder.Companion.of
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

/**
 * @author sky_lock
 */
class PlayerSkull private constructor(private val player: OfflinePlayer, private val amount: Int) {

    fun toItemStack(): ItemStack {
        val itemStack = ItemStack(Material.PLAYER_HEAD, amount)
        val name: String = player.name ?: "unknown"
        val meta = requireNotNull(itemStack.itemMeta as SkullMeta)
        meta.owningPlayer = player
        itemStack.itemMeta = meta
        return of(itemStack).name(name).build()
    }

    companion object {
        @JvmStatic
        fun of(owner: UUID, amount: Int): PlayerSkull {
            return PlayerSkull(Bukkit.getOfflinePlayer(owner), amount)
        }
    }

}