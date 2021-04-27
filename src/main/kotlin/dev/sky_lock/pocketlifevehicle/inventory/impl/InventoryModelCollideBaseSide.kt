package dev.sky_lock.pocketlifevehicle.inventory.impl

import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.inventory.InventoryCustom
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */

class InventoryModelCollideBaseSide(private val player: Player, private val model: Model): InventoryCustom(18, "当たり判定(底辺)")  {

    init {
        for (i in 0..8) {
            val baseSide = 0.5 * (i + 1)
            setSlot(i, ItemStackBuilder(Material.LIGHT_BLUE_CONCRETE, 1).setName(baseSide.toString()).build()) { event ->
                model.size.baseSide = baseSide.toFloat()
                addSelectGrowEffectToSingleItem(event)
            }
        }
        val backBarrier = ItemStackBuilder(Material.BARRIER, 1).setName(ChatColor.RED + "戻る").build()
        setSlot(13, backBarrier) {
            player.openInventory(InventoryModelOption(player, model))
        }
    }
}