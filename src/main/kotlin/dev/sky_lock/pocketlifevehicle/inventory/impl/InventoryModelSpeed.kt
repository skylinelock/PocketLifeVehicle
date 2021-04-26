package dev.sky_lock.pocketlifevehicle.inventory.impl

import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.inventory.InventoryCustom
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.vehicle.model.MaxSpeed
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author sky_lock
 */

class InventoryModelSpeed(private val player: Player, private val model: Model): InventoryCustom(18, "最高速度") {

    init {
        setSlot(0, speedItem(MaxSpeed.SLOWEST)) {

        }
        setSlot(2, speedItem(MaxSpeed.SLOW)) {

        }
        setSlot(4, speedItem(MaxSpeed.NORMAL)) {

        }
        setSlot(6, speedItem(MaxSpeed.FAST)) {

        }
        setSlot(8, speedItem(MaxSpeed.FASTEST)) {

        }

        val backBarrier = ItemStackBuilder(Material.BARRIER, 1).setName(ChatColor.RED + "戻る").build()
        setSlot(13, backBarrier) {
            player.openInventory(InventoryModelOption(player, model))
        }
    }

    private fun speedItem(speed: MaxSpeed): ItemStack {
        return ItemStackBuilder(Material.SEA_LANTERN, 1)
            .setName(speed.label)
            .setLore(ChatColor.GRAY + "- 約" + speed.forTick(20).toString() + "blocks/s")
            .build()
    }
}