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

class InventoryModelFuel(private val player: Player, private val model: Model): InventoryCustom(54, "燃料上限") {

    init {
        var index = 1
        for (i in 0..4) {
            for (j in 0..8) {
                if (j % 2 == 0) {
                    val slot = 9 * i + j
                    val fuel = index * 25
                    val iron = ItemStackBuilder(Material.IRON_BLOCK, 1).setName(fuel.toString()).build()
                    setSlot(slot, iron) {

                    }
                    index++
                }
            }
        }
        val backBarrier = ItemStackBuilder(Material.BARRIER, 1).setName(ChatColor.RED + "戻る").build()
        setSlot(49, backBarrier) {
            player.openInventory(InventoryModelOption(player, model))
        }
    }


}