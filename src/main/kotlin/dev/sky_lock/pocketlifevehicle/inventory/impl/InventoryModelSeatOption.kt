package dev.sky_lock.pocketlifevehicle.inventory.impl

import dev.sky_lock.pocketlifevehicle.inventory.InventoryCustom
import dev.sky_lock.pocketlifevehicle.inventory.openModelTextEditor
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.text.Line
import dev.sky_lock.pocketlifevehicle.vehicle.model.Capacity
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */

class InventoryModelSeatOption(private val player: Player, private val model: Model): InventoryCustom(27, "座席設定") {

    init {
        val singlePlanksBuilder = ItemStackBuilder(Material.OAK_PLANKS, 1).setName(Line().raw("1"))
        if (model.seatOption.capacity == Capacity.SINGLE) {
            singlePlanksBuilder.addGlowEffect()
        }
        setSlot(2, singlePlanksBuilder.build()) { event ->
            model.seatOption.capacity = Capacity.SINGLE
            addSelectGrowEffectToSingleItem(event)
        }

        val doublePlanksBuilder = ItemStackBuilder(Material.SPRUCE_PLANKS, 1).setName(Line().raw("2"))
        if (model.seatOption.capacity == Capacity.DOUBLE) {
            doublePlanksBuilder.addGlowEffect()
        }
        setSlot(4, doublePlanksBuilder.build()) { event ->
            model.seatOption.capacity = Capacity.DOUBLE
            addSelectGrowEffectToSingleItem(event)
        }

        val quadPlanksBuilder = ItemStackBuilder(Material.BIRCH_PLANKS, 1).setName(Line().raw("4"))
        if (model.seatOption.capacity == Capacity.QUAD) {
            quadPlanksBuilder.addGlowEffect()
        }
        setSlot(6, quadPlanksBuilder.build()) { event ->
            model.seatOption.capacity = Capacity.QUAD
            addSelectGrowEffectToSingleItem(event)
        }

        val offsetGlass = ItemStackBuilder(Material.WHITE_STAINED_GLASS, 1).setName(Line().raw("オフセット")).build()
        setSlot(11, offsetGlass) {
            player.openModelTextEditor("オフセット", model.seatOption.offset.toString(), ContainerModelTextEdit.ModifyType.OFFSET, model)
        }

        val depthGlass = ItemStackBuilder(Material.LIGHT_BLUE_STAINED_GLASS, 1).setName(Line().raw("奥行き")).build()
        setSlot(13, depthGlass) {
            player.openModelTextEditor("奥行き", model.seatOption.depth.toString(), ContainerModelTextEdit.ModifyType.DEPTH, model)
        }

        val widthGlass = ItemStackBuilder(Material.PINK_STAINED_GLASS, 1).setName(Line().raw("幅")).build()
        setSlot(15, widthGlass) {
            player.openModelTextEditor("幅", model.seatOption.width.toString(), ContainerModelTextEdit.ModifyType.WIDTH, model)
        }

        val backBarrier = ItemStackBuilder(Material.BARRIER, 1).setName(Line().red("戻る")).build()
        setSlot(22, backBarrier) {
            player.openInventory(InventoryModelOption(player, model))
        }
    }
}