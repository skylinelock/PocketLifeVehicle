package dev.sky_lock.pocketlifevehicle.inventory.impl

import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.extension.chat.sendVehiclePrefixedMessage
import dev.sky_lock.pocketlifevehicle.inventory.InventoryCustom
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.vehicle.ModelRegistry
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleManager
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author sky_lock
 */

class InventoryModelOption(private val player: Player, private val model: Model): InventoryCustom(45, "モデル設定") {

    init {
        val deleteRedStone = ItemStackBuilder(Material.REDSTONE, 1).setName(ChatColor.RED + "削除する").build()
        setSlot(3, deleteRedStone) {
            player.openInventory(InventoryConfirmDelete(
                {
                    player.openInventory(InventoryModelOption(player, model))
                },
                {
                    val id = model.id
                    ModelRegistry.unregister(id)
                    VehicleManager.scrapAll(id)
                    player.sendVehiclePrefixedMessage(ChatColor.GREEN + id + "を削除しました")
                    player.openInventory(InventoryListModel(player))
                }
            ))
        }

        val recreateEmerald = optionItem(Material.EMERALD, ChatColor.GREEN + "ID(" + ChatColor.YELLOW + model.id + ChatColor.GREEN + ")を変更する", ChatColor.RED + "※モデルは同じ設定で再作成されます")
        setSlot(5, recreateEmerald) {

        }

        val nameTag = optionItem(Material.NAME_TAG, ChatColor.GREEN + "名前", ChatColor.YELLOW + model.name)
        setSlot(11, nameTag) {

        }

        val loreSign = optionItem(Material.OAK_SIGN, ChatColor.GREEN + "説明")
        setSlot(13, loreSign) {

        }

        val heightArmor = optionItem(Material.IRON_HORSE_ARMOR, ChatColor.GREEN + "座高", ChatColor.YELLOW + model.height.toString())
        setSlot(15, heightArmor) {

        }
        val spec = model.spec
        val fuelCoalBlock = optionItem(Material.COAL_BLOCK, ChatColor.GREEN + "燃料上限", ChatColor.YELLOW + spec.maxFuel.toString())
        setSlot(20, fuelCoalBlock) {

        }

        val speedDiamond = optionItem(Material.DIAMOND, ChatColor.GREEN + "最高速度", ChatColor.YELLOW + spec.maxSpeed.label)
        setSlot(22, speedDiamond) {

        }

        val capacitySaddle = optionItem(Material.SADDLE, ChatColor.GREEN + "乗車人数", ChatColor.YELLOW + model.capacity.value().toString())
        setSlot(24, capacitySaddle) {

        }

        val collideBeacon = optionItem(Material.BEACON, ChatColor.GREEN + "当たり判定")
        setSlot(29, collideBeacon) {

        }

        val armorStand = optionItem(Material.ARMOR_STAND, ChatColor.GREEN + "アーマースタンド")
        setSlot(31, armorStand) {

        }

        val flagRepeater = optionItem(Material.REPEATER, ChatColor.GREEN + "フラグ")
        setSlot(33, flagRepeater) {

        }

        val backBarrier = optionItem(Material.BARRIER, ChatColor.RED + "戻る")
        setSlot(40, backBarrier) {
            player.openInventory(InventoryListModel(player))
        }
    }

    private fun optionItem(material: Material, title: String, vararg defaults: String): ItemStack {
        return ItemStackBuilder(material, 1).setName(title).setLore(*defaults).build()
    }


}