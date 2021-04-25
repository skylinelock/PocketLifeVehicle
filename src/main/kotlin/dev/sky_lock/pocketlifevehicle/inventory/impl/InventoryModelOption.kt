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

class InventoryModelOption(private val player: Player, private val model: Model): InventoryCustom(45, "モデル設定") {

    init {
        val deleteRedStone = ItemStackBuilder(Material.REDSTONE, 1).setName(ChatColor.RED + "削除する").build()
        setSlot(3, deleteRedStone) {

        }

        val recreateEmerald = ItemStackBuilder(Material.EMERALD, 1).setName(ChatColor.GREEN + "IDを変更して再構成する").build()
        setSlot(5, recreateEmerald) {

        }

        val nameTag = ItemStackBuilder(Material.NAME_TAG, 1).setName("名前").build()
        setSlot(11, nameTag) {

        }

        val loreSign = ItemStackBuilder(Material.OAK_SIGN, 1).setName("説明").build()
        setSlot(13, loreSign) {

        }

        val heightArmor = ItemStackBuilder(Material.IRON_HORSE_ARMOR, 1).setName("座高").build()
        setSlot(15, heightArmor) {

        }

        val fuelCoalBlock = ItemStackBuilder(Material.COAL_BLOCK, 1).setName("燃料上限").build()
        setSlot(20, fuelCoalBlock) {

        }

        val speedDiamond = ItemStackBuilder(Material.DIAMOND, 1).setName("最高速度").build()
        setSlot(22, speedDiamond) {

        }

        val capacitySaddle = ItemStackBuilder(Material.SADDLE, 1).setName("乗車人数").build()
        setSlot(24, capacitySaddle) {

        }

        val collideBeacon = ItemStackBuilder(Material.BEACON, 1).setName("当たり判定").build()
        setSlot(29, collideBeacon) {

        }

        val armorStand = ItemStackBuilder(Material.ARMOR_STAND, 1).setName("アーマースタンド").build()
        setSlot(31, armorStand) {

        }

        val flagRepeater = ItemStackBuilder(Material.REPEATER, 1).setName("フラグ").build()
        setSlot(33, flagRepeater) {

        }

        val backBarrier = ItemStackBuilder(Material.BARRIER, 1).setName("戻る").build()
        setSlot(40, backBarrier) {
            player.openInventory(InventoryListModel(player))
        }
    }


}