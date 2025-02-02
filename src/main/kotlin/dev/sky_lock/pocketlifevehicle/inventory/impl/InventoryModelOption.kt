package dev.sky_lock.pocketlifevehicle.inventory.impl

import dev.sky_lock.pocketlifevehicle.inventory.InventoryCustom
import dev.sky_lock.pocketlifevehicle.inventory.ext.openModelLoreEditor
import dev.sky_lock.pocketlifevehicle.inventory.ext.openModelTextEditor
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.text.Line
import dev.sky_lock.pocketlifevehicle.text.ext.sendVehiclePrefixedSuccessMessage
import dev.sky_lock.pocketlifevehicle.vehicle.entity.VehicleManager
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import dev.sky_lock.pocketlifevehicle.vehicle.model.ModelRegistry
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author sky_lock
 */

class InventoryModelOption(private val player: Player, private val model: Model): InventoryCustom(54, "モデル設定") {

    init {
        val deleteRedStone = ItemStackBuilder(Material.REDSTONE, 1).setName(Line().red("削除する")).build()
        setSlot(8, deleteRedStone) {
            player.openInventory(InventoryConfirmDelete(
                {
                    player.openInventory(InventoryModelOption(player, model))
                },
                {
                    val id = model.id
                    ModelRegistry.unregister(id)
                    VehicleManager.scrapAll(id)
                    player.sendVehiclePrefixedSuccessMessage("${id}を削除しました")
                    player.openInventory(InventoryModelList(player))
                }
            ))
        }

        val recreateEmerald = optionItem(Material.EMERALD, Line().green("ID(").yellow(model.id).green(")を変更する"), Line().red("※モデルは同じ設定で再作成されます"))
        setSlot(17, recreateEmerald) {
            player.openModelTextEditor("ID", "id", ContainerModelTextEdit.ModifyType.ID_RECREATE, model)
        }

        val paper = optionItem(Material.PAPER, Line().green("モデルを複製する"))
        setSlot(26, paper) {
            player.openModelTextEditor("ID", "id", ContainerModelTextEdit.ModifyType.ID_COPY, model)
        }

        val name = Line().colorCoded(model.name)
        val nameTag = optionItem(Material.NAME_TAG, Line().green("名前"), name)
        setSlot(11, nameTag) {
            player.openModelTextEditor("名前設定", name.toColorCodedText(), ContainerModelTextEdit.ModifyType.NAME, model)
        }

        val loreSign = optionItem(Material.OAK_SIGN, Line().green("説明"), *model.lore.map { lore -> Line().colorCoded(lore)}.toTypedArray() )
        setSlot(13, loreSign) {
            player.openModelLoreEditor(model)
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

        val specBottle = optionItem(
            Material.EXPERIENCE_BOTTLE,
            Line().green("スペック"),
            Line().gold("最高速度: ").yellow(model.spec.maxSpeed.label),
            Line().gold("ステアリング性能: ").yellow(model.spec.steeringLevel.label)
        )
        setSlot(22, specBottle) {
            player.openInventory(InventoryModelSpec(player, model))
        }

        val capacitySaddle = optionItem(Material.SADDLE,
            Line().green("座席"),
            Line().gold("乗車人数: ").yellow(model.seatOption.capacity.value().toString()),
            Line().gold("オフセット: ").yellow(model.seatOption.offset.toString()),
            Line().gold("奥行き: ").yellow(model.seatOption.depth.toString()),
            Line().gold("幅: ").yellow(model.seatOption.width.toString()),
        )
        setSlot(24, capacitySaddle) {
            player.openInventory(InventoryModelSeatOption(player, model))
        }

        val collideBeacon = optionItem(Material.BEACON,
            Line().green("当たり判定"),
            Line().gold("底辺: ").yellow(model.size.baseSide.toString()),
            Line().gold("高さ: ").yellow(model.size.height.toString())
        )
        setSlot(29, collideBeacon) {
            player.openInventory(InventoryModelCollideBox(player, model))
        }

        val armorStand = optionItem(Material.ARMOR_STAND,
            Line().green("3Dモデル"),
            Line().gold("大きさ: ").yellow(model.modelOption.bigText()),
            Line().gold("アイテム位置: ").yellow(model.modelOption.position.label),
            Line().gold("アイテム: ").yellow(model.modelOption.id.toString()),
        )
        setSlot(31, armorStand) {
            player.openInventory(InventoryModelArmorStand(player, model))
        }

        val flagRepeater = optionItem(
            Material.REPEATER,
            Line().green("フラグ"),
            Line().gold("エンジン音: ").yellow(model.flag.engineSoundText()),
            Line().gold("ハンドリングのアニメーション: ").yellow(model.flag.animationText()),
            Line().gold("燃料消費: ").yellow(model.flag.consumeFuelText()),
            Line().gold("イベント仕様: ").yellow(model.flag.eventOnlyText())
        )
        setSlot(33, flagRepeater) {
            player.openInventory(InventoryModelFlag(player, model))
        }

        val backBarrier = optionItem(Material.BARRIER, Line().red("戻る"))
        setSlot(49, backBarrier) {
            player.openInventory(InventoryModelList(player))
        }
    }

    private fun optionItem(material: Material, title: Line, vararg defaults: Line): ItemStack {
        return ItemStackBuilder(material, 1).setName(title).setLore(*defaults).build()
    }


}