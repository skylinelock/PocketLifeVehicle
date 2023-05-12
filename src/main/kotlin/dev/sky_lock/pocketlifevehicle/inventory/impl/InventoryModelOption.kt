package dev.sky_lock.pocketlifevehicle.inventory.impl

import dev.sky_lock.pocketlifevehicle.extension.chat.Line
import dev.sky_lock.pocketlifevehicle.extension.chat.sendVehiclePrefixedSuccessMessage
import dev.sky_lock.pocketlifevehicle.inventory.InventoryCustom
import dev.sky_lock.pocketlifevehicle.inventory.openModelLoreEditor
import dev.sky_lock.pocketlifevehicle.inventory.openModelTextEditor
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.vehicle.ModelRegistry
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleManager
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author sky_lock
 */

class InventoryModelOption(private val player: Player, private val model: Model): InventoryCustom(45, "モデル設定") {

    init {
        val deleteRedStone = ItemStackBuilder(Material.REDSTONE, 1).setName(Line().red("削除する")).build()
        setSlot(3, deleteRedStone) {
            player.openInventory(InventoryConfirmDelete(
                {
                    player.openInventory(InventoryModelOption(player, model))
                },
                {
                    val id = model.id
                    ModelRegistry.unregister(id)
                    VehicleManager.scrapAll(id)
                    player.sendVehiclePrefixedSuccessMessage(id + "を削除しました")
                    player.openInventory(InventoryModelList(player))
                }
            ))
        }

        val recreateEmerald = optionItem(Material.EMERALD, Line().green("ID(").yellow(model.id).green(")を変更する"), Line().red("※モデルは同じ設定で再作成されます"))
        setSlot(5, recreateEmerald) {
            player.openModelTextEditor("ID", "id", ContainerModelTextEdit.ModifyType.ID, model)
        }

        val nameTag = optionItem(Material.NAME_TAG, Line().green("名前"), Line().colorCoded(model.name))
        setSlot(11, nameTag) {
            player.openModelTextEditor("名前設定", "", ContainerModelTextEdit.ModifyType.NAME, model)
        }

        val loreSign = optionItem(Material.OAK_SIGN, Line().green("説明"), *model.lore.map { lore -> Line().colorCoded(lore)}.toTypedArray() )
        setSlot(13, loreSign) {
            player.openModelLoreEditor("", model)
        }

        val heightArmor = optionItem(Material.IRON_HORSE_ARMOR, Line().green("座高"), Line().yellow(model.height.toString()))
        setSlot(15, heightArmor) {
            player.openModelTextEditor("座高設定", model.height.toString(), ContainerModelTextEdit.ModifyType.HEIGHT, model)
        }
        val spec = model.spec
        val fuelCoalBlock = optionItem(Material.COAL_BLOCK, Line().green("燃料上限"), Line().yellow(spec.maxFuel.toString()))
        setSlot(20, fuelCoalBlock) {
            player.openInventory(InventoryModelFuel(player, model))
        }

        val specBottle = optionItem(Material.EXPERIENCE_BOTTLE, Line().green("スペック"))
        setSlot(22, specBottle) {
            player.openInventory(InventoryModelSpec(player, model))
        }

        val capacitySaddle = optionItem(Material.SADDLE, Line().green("座席"), Line().yellow(model.seatOption.capacity.value().toString()))
        setSlot(24, capacitySaddle) {
            player.openInventory(InventoryModelSeatOption(player, model))
        }

        val collideBeacon = optionItem(Material.BEACON, Line().green("当たり判定"))
        setSlot(29, collideBeacon) {
            player.openInventory(InventoryModelCollideBox(player, model))
        }

        val armorStand = optionItem(Material.ARMOR_STAND, Line().green("3Dモデル"))
        setSlot(31, armorStand) {
            player.openInventory(InventoryModelArmorStand(player, model))
        }

        val flagRepeater = optionItem(Material.REPEATER, Line().green("フラグ"))
        setSlot(33, flagRepeater) {
            player.openInventory(InventoryModelFlag(player, model))
        }

        val backBarrier = optionItem(Material.BARRIER, Line().red("戻る"))
        setSlot(40, backBarrier) {
            player.openInventory(InventoryModelList(player))
        }
    }

    private fun optionItem(material: Material, title: Line, vararg defaults: Line): ItemStack {
        return ItemStackBuilder(material, 1).setName(title).setLore(*defaults).build()
    }


}