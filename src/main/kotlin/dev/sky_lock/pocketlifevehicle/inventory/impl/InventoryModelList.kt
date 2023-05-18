package dev.sky_lock.pocketlifevehicle.inventory.impl

import dev.sky_lock.pocketlifevehicle.inventory.InventoryCustom
import dev.sky_lock.pocketlifevehicle.inventory.ext.openModelTextEditor
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.text.Line
import dev.sky_lock.pocketlifevehicle.vehicle.ModelRegistry
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author sky_lock
 */

class InventoryModelList(private val player: Player): InventoryCustom(27, "モデル一覧") {
    private var page = 1
    private val models = ModelRegistry.set().toList()

    init {
        val addVehicleCart = ItemStackBuilder(Material.CHEST_MINECART, 1).setName(Line().green("車種を追加する")).build()
        setSlot(22, addVehicleCart) {
            player.openModelTextEditor("ID", "id", ContainerModelTextEdit.ModifyType.ID_CREATE, null)
        }
        setModelSlots()
    }

    private fun clearModelSlots() {
        for (i in 0..17) {
            clear(i)
        }
        clear(21)
        clear(23)
    }

    private fun setModelSlots() {
        val start = (page - 1) * 18
        val end = page * 18 - 1
        var count = 0
        for (model in models) {
            if (count < start) {
                count++
                continue
            }
            if (count > end) {
                val next = ItemStackBuilder(Material.IRON_NUGGET, 1).setName(Line().aqua("次のページ")).setCustomModelData(16).build()
                setSlot(23, next) {
                    page++
                    clearModelSlots()
                    setModelSlots()
                }
                break
            }
            setSlot(count % 18, modelItem(model)) {
                player.openInventory(InventoryModelOption(player, model))
            }
            count++
        }

        if (page > 1) {
            val back =
                ItemStackBuilder(Material.IRON_NUGGET, 1).setName(Line().aqua("前のページ")).setCustomModelData(17)
                    .build()
            setSlot(21, back) {
                if (page > 1) page--
                clearModelSlots()
                setModelSlots()
            }
        }
    }

    private fun modelItem(model: Model): ItemStack {
        val desc = mutableListOf<Line>()
        desc.add(Line().darkAqua("名前: ").colorCoded(model.name))
        desc.add(Line().darkAqua("説明: "))
        model.lore.forEach { line -> desc.add(Line().darkGray("- ").colorCoded(line)) }
        val spec = model.spec
        desc.add(Line().darkAqua("燃料上限: ").aqua(spec.maxFuel.toString()))
        desc.add(Line().darkAqua("最高速度: ").aqua(spec.maxSpeed.label))
        desc.add(Line().darkAqua("ステアリング性能: ").aqua(spec.steeringLevel.label))
        val box = model.size
        desc.add(Line().darkAqua("当たり判定(高さ): ").aqua(box.height.toString()))
        desc.add(Line().darkAqua("当たり判定(底辺): ").aqua(box.baseSide.toString()))
        desc.add(Line().darkAqua("座高: ").aqua(model.height.toString()))
        val modelOption = model.modelOption
        desc.add(Line().darkAqua("モデル位置: ").aqua(modelOption.position.label))
        val size = if (model.modelOption.isBig) "大きい" else "小さい"
        desc.add(Line().darkAqua("大きさ: ").aqua(size))
        val seatOption = model.seatOption
        desc.add(Line().darkAqua("乗車人数: ").aqua(seatOption.capacity.value().toString()))
        desc.add(Line().darkAqua("オフセット: ").aqua(seatOption.offset.toString()))
        desc.add(Line().darkAqua("座席間距離(縦): ").aqua(seatOption.depth.toString()))
        desc.add(Line().darkAqua("座席間距離(横): ").aqua(seatOption.width.toString()))
        val flag = model.flag
        desc.add(Line().darkAqua("エンジン音: ").aqua(flag.engineSoundText()))
        desc.add(Line().darkAqua("アニメーション: " ).aqua(flag.animationText()))
        desc.add(Line().darkAqua("燃料消費: ").aqua(flag.consumeFuelText()))
        desc.add(Line().darkAqua("イベント仕様: ").aqua(flag.eventOnlyText()))
        return ItemStackBuilder(model.itemStack).setName(Line().yellow(model.id)).setLore(desc).build()
    }
}