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

class InventoryModelArmorStand(private val player: Player, private val model: Model): InventoryCustom(18, "3Dモデル設定") {

    init {
        val sizePerl = ItemStackBuilder(Material.ENDER_PEARL, 1).setName(ChatColor.GREEN + "大きさ").build()
        setSlot(2, sizePerl) {

        }

        val itemPosBall = ItemStackBuilder(Material.SLIME_BALL, 1).setName(ChatColor.GREEN + "アイテム位置").build()
        setSlot(4, itemPosBall) {

        }

        val itemCream = ItemStackBuilder(Material.MAGMA_CREAM, 1).setName(ChatColor.GREEN + "アイテム").build()
        setSlot(6, itemCream) {

        }

        val backBarrier = ItemStackBuilder(Material.BARRIER, 1).setName(ChatColor.RED + "戻る").build()
        setSlot(13, backBarrier) {
            player.openInventory(InventoryModelOption(player, model))
        }
    }
}