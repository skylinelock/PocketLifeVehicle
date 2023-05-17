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

class InventoryModelSpec(private val player: Player, private val model: Model): InventoryCustom(18, "スペック") {

    init {
        val spec = model.spec
        val speedDiamond = ItemStackBuilder(Material.DIAMOND, 1).setName(Line().green("最高速度")).setLore(Line().yellow(spec.maxSpeed.label)).build()
        setSlot(3, speedDiamond) {
            player.openInventory(InventoryModelSpeed(player, model))
        }

        val steeringClock = ItemStackBuilder(Material.CLOCK, 1).setName(Line().green("ステアリング性能")).setLore(Line().yellow(spec.steeringLevel.label)).build()
        setSlot(5, steeringClock) {
            player.openInventory(InventoryModelSteering(player, model))
        }

        val backBarrier = ItemStackBuilder(Material.BARRIER, 1).setName(Line().red("戻る")).build()
        setSlot(13, backBarrier) {
            player.openInventory(InventoryModelOption(player, model))
        }
    }
}