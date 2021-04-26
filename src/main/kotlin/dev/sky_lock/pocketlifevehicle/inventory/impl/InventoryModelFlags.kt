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

class InventoryModelFlags(private val player: Player, private val model: Model): InventoryCustom(18, "フラグ設定") {

    init {
        // 切り替え可能かなし
        val engineBell = ItemStackBuilder(Material.BELL, 1).setName(ChatColor.GREEN + "エンジン音").build()
        setSlot(2, engineBell) {

        }

        val animationDye = ItemStackBuilder(Material.YELLOW_DYE, 1).setName(ChatColor.GREEN + "ハンドリングのアニメーション").build()
        setSlot(4, animationDye) {
        }

        //ありかなし
        val fuelCoal = ItemStackBuilder(Material.CHARCOAL, 1).setName(ChatColor.GREEN + "燃料消費").build()
        setSlot(6, fuelCoal) {

        }

        val backBarrier = ItemStackBuilder(Material.BARRIER, 1).setName(ChatColor.RED + "戻る").build()
        setSlot(13, backBarrier) {
            player.openInventory(InventoryModelOption(player, model))
        }
    }
}