package dev.sky_lock.pocketlifevehicle.inventory.impl

import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.inventory.InventoryCustom
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.vehicle.model.Capacity
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */

class InventoryModelSeatOption(private val player: Player, private val model: Model): InventoryCustom(27, "座席設定") {

    init {
        val singlePlanks = ItemStackBuilder(Material.OAK_PLANKS, 1).setName("1").build()
        setSlot(2, singlePlanks) { event ->
            model.seatOption.capacity = Capacity.SINGLE
            addSelectGrowEffectToSingleItem(event)
        }

        val doublePlanks = ItemStackBuilder(Material.SPRUCE_PLANKS, 1).setName("2").build()
        setSlot(4, doublePlanks) { event ->
            model.seatOption.capacity = Capacity.DOUBLE
            addSelectGrowEffectToSingleItem(event)
        }

        val quadPlanks = ItemStackBuilder(Material.BIRCH_PLANKS, 1).setName("4").build()
        setSlot(6, quadPlanks) { event ->
            model.seatOption.capacity = Capacity.QUAD
            addSelectGrowEffectToSingleItem(event)
        }

        val offsetGlass = ItemStackBuilder(Material.WHITE_STAINED_GLASS, 1).setName("オフセット").build()
        setSlot(11, offsetGlass) {

        }

        val depthGlass = ItemStackBuilder(Material.LIGHT_BLUE_STAINED_GLASS, 1).setName("奥行き").build()
        setSlot(13, depthGlass) {

        }

        val widthGlass = ItemStackBuilder(Material.PINK_STAINED_GLASS, 1).setName("幅").build()
        setSlot(15, widthGlass) {

        }

        val backBarrier = ItemStackBuilder(Material.BARRIER, 1).setName(ChatColor.RED + "戻る").build()
        setSlot(22, backBarrier) {
            player.openInventory(InventoryModelOption(player, model))
        }
    }
}