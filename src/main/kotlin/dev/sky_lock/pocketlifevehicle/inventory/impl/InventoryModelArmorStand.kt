package dev.sky_lock.pocketlifevehicle.inventory.impl

import dev.sky_lock.pocketlifevehicle.extension.chat.Line
import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.inventory.InventoryCustom
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */

class InventoryModelArmorStand(private val player: Player, private val model: Model): InventoryCustom(18, "3Dモデル設定") {

    init {
        val builder = ItemStackBuilder(Material.ENDER_PEARL, 1).setName(Line().green("大きさ"))
        if (model.modelOption.isBig) builder.setLore(Line().yellow("大きい")) else builder.setLore(Line().yellow("小さい"))
        setSlot(2, builder.build()) { event ->
            model.modelOption.isBig = !model.modelOption.isBig
            if (model.modelOption.isBig) builder.setLore(Line().yellow("大きい")) else builder.setLore(Line().yellow("小さい"))
            event.currentItem = builder.build()
        }

        val itemPosBall = ItemStackBuilder(Material.SLIME_BALL, 1).setName(Line().green("アイテム位置")).build()
        setSlot(4, itemPosBall) {
            player.openInventory(InventoryModelItemPosition(player, model))
        }

        val itemCream = ItemStackBuilder(Material.MAGMA_CREAM, 1).setName(Line().green("アイテム")).build()
        setSlot(6, itemCream) {
            player.openInventory(InventoryModelItem(player, model))
        }

        val backBarrier = ItemStackBuilder(Material.BARRIER, 1).setName(Line().red("戻る")).build()
        setSlot(13, backBarrier) {
            player.openInventory(InventoryModelOption(player, model))
        }
    }
}