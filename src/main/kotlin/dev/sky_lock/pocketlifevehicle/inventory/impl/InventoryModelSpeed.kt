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
        for (i in 0..4) {
            val maxSpeed = MaxSpeed.values()[i]
            val speedItem = speedItem(maxSpeed, model.spec.maxSpeed == maxSpeed)
            setSlot(2 * i, speedItem) { event ->
                model.spec.maxSpeed = maxSpeed
                addSelectGrowEffectToSingleItem(event)
            }
        }

        val backBarrier = ItemStackBuilder(Material.BARRIER, 1).setName(ChatColor.RED + "戻る").build()
        setSlot(13, backBarrier) {
            player.openInventory(InventoryModelOption(player, model))
        }
    }

    private fun speedItem(speed: MaxSpeed, glow: Boolean): ItemStack {
        val builder = ItemStackBuilder(Material.SEA_LANTERN, 1)
            .setName(speed.label)
            .setLore(ChatColor.GRAY + "- 約" + speed.forTick(20).toString() + "blocks/s")
        if (glow) builder.addGlowEffect()
        return builder.build()
    }
}