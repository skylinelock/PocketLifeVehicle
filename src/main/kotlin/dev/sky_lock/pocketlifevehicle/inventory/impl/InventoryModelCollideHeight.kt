package dev.sky_lock.pocketlifevehicle.inventory.impl

import dev.sky_lock.pocketlifevehicle.extension.chat.Line
import dev.sky_lock.pocketlifevehicle.inventory.InventoryCustom
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */

class InventoryModelCollideHeight(private val player: Player, private val model: Model): InventoryCustom(18, "当たり判定(高さ)") {

    init {
        for (i in 0..8) {
            val height = (0.5 * (i + 1)).toFloat()
            val builder = ItemStackBuilder(Material.YELLOW_CONCRETE, 1).setName(height.toString())
            if (model.size.height == height) {
                builder.addGlowEffect()
            }
            setSlot(i, builder.build()) { event ->
                model.size.height = height
                addSelectGrowEffectToSingleItem(event)
            }
        }
        val backBarrier = ItemStackBuilder(Material.BARRIER, 1).setName(Line().red("戻る")).build()
        setSlot(13, backBarrier) {
            player.openInventory(InventoryModelCollideBox(player, model))
        }
    }
}