package dev.sky_lock.pocketlifevehicle.inventory.impl

import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.inventory.InventoryCustom
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import dev.sky_lock.pocketlifevehicle.vehicle.model.SteeringLevel
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author sky_lock
 */

class InventoryModelSteering(private val player: Player, private val model: Model): InventoryCustom(18, "ステアリング性能") {
    init {
        for (i in 0..4) {
            val steering = SteeringLevel.values()[i]
            val speedItem = steeringItem(steering, model.spec.steeringLevel == steering)
            setSlot(2 * i, speedItem) { event ->
                model.spec.steeringLevel = steering
                addSelectGrowEffectToSingleItem(event)
            }
        }

        val backBarrier = ItemStackBuilder(Material.BARRIER, 1).setName(ChatColor.RED + "戻る").build()
        setSlot(13, backBarrier) {
            player.openInventory(InventoryModelSpec(player, model))
        }
    }

    private fun steeringItem(steering: SteeringLevel, glow: Boolean): ItemStack {
        val builder = ItemStackBuilder(Material.BEDROCK, 1)
            .setName(steering.label)
        if (glow) builder.addGlowEffect()
        return builder.build()
    }
}