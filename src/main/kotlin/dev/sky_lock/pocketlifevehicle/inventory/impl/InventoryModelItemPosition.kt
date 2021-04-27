package dev.sky_lock.pocketlifevehicle.inventory.impl

import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.inventory.InventoryCustom
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.vehicle.model.ItemPosition
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */

class InventoryModelItemPosition(private val player: Player, private val model: Model): InventoryCustom(18, "アイテム位置") {

    init {
        val modelOption = model.modelOption
        val helmetBuilder = ItemStackBuilder(Material.DIAMOND_HELMET, 1).setName(ChatColor.GREEN + "頭")
        if (modelOption.position == ItemPosition.HEAD) {
            helmetBuilder.addGlowEffect()
        }
        setSlot(0, helmetBuilder.build()) { event ->
            model.modelOption.position = ItemPosition.HEAD
            addSelectGrowEffectToSingleItem(event)
        }

        val chestPlateBuilder = ItemStackBuilder(Material.DIAMOND_CHESTPLATE, 1).setName(ChatColor.GREEN + "胸")
        if (modelOption.position == ItemPosition.CHEST) {
            chestPlateBuilder.addGlowEffect()
        }
        setSlot(2, chestPlateBuilder.build()) { event ->
            model.modelOption.position = ItemPosition.CHEST
            addSelectGrowEffectToSingleItem(event)
        }

        val leggingsBuilder = ItemStackBuilder(Material.DIAMOND_LEGGINGS, 1).setName(ChatColor.GREEN + "膝")
        if (modelOption.position == ItemPosition.LEGS) {
            leggingsBuilder.addGlowEffect()
        }
        setSlot(4, leggingsBuilder.build()) { event ->
            model.modelOption.position = ItemPosition.LEGS
            addSelectGrowEffectToSingleItem(event)
        }

        val bootsBuilder = ItemStackBuilder(Material.DIAMOND_BOOTS, 1).setName(ChatColor.GREEN + "足")
        if (modelOption.position == ItemPosition.FEET) {
            bootsBuilder.addGlowEffect()
        }
        setSlot(6, bootsBuilder.build()) { event ->
            model.modelOption.position = ItemPosition.FEET
            addSelectGrowEffectToSingleItem(event)
        }

        val swordBuilder = ItemStackBuilder(Material.DIAMOND_SWORD, 1).setName(ChatColor.GREEN + "手")
        if (modelOption.position == ItemPosition.HAND) {
            swordBuilder.addGlowEffect()
        }
        setSlot(8, swordBuilder.build()) { event ->
            model.modelOption.position = ItemPosition.HAND
            addSelectGrowEffectToSingleItem(event)
        }

        val backBarrier = ItemStackBuilder(Material.BARRIER, 1).setName(ChatColor.RED + "戻る").build()
        setSlot(13, backBarrier) {
            player.openInventory(InventoryModelArmorStand(player, model))
        }
    }
}