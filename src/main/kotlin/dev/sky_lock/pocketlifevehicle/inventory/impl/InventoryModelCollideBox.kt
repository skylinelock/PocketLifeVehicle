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

class InventoryModelCollideBox(private val player: Player, private val model: Model): InventoryCustom(18, "当たり判定") {

    private val baseSideItem = ItemStackBuilder(Material.LIGHT_BLUE_CONCRETE, 1).setName("底辺").build()
    private val heightItem = ItemStackBuilder(Material.YELLOW_CONCRETE, 1).setName("高さ").build()

    init {
        setSlot(3, baseSideItem) {
            player.openInventory(InventoryModelCollideBaseSide(player, model))
        }

        setSlot(5, heightItem) {
            player.openInventory(InventoryModelCollideHeight(player, model))
        }

        val backBarrier = ItemStackBuilder(Material.BARRIER, 1).setName(Line().red("戻る")).build()
        setSlot(13, backBarrier) {
            player.openInventory(InventoryModelOption(player, model))
        }
    }
}