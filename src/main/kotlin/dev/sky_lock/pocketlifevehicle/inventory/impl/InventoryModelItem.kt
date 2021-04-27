package dev.sky_lock.pocketlifevehicle.inventory.impl

import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.inventory.InventoryCustom
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */

class InventoryModelItem(private val player: Player, private val model: Model) : InventoryCustom(27, "モデルアイテム") {
    private var page = 1

    init {
        setItems()

        val backBarrier = ItemStackBuilder(Material.BARRIER, 1).setName(ChatColor.RED + "戻る").build()
        setSlot(22, backBarrier) {
            player.openInventory(InventoryModelArmorStand(player, model))
        }
    }

    private fun clearItemSlots() {
        for (i in 0..17) {
            clear(i)
        }
        clear(21)
        clear(23)
    }

    private fun setItems() {
        val start = (page - 1) * 18
        val end = page * 18 - 1
        var count = 0
        for (i in 0..89) {
            if (count < start) {
                count++
                continue
            }
            if (count > end) {
                val next =
                    ItemStackBuilder(Material.IRON_NUGGET, 1).setName(ChatColor.AQUA + "次のページ").setCustomModelData(16)
                        .build()
                setSlot(23, next) {
                    page++
                    clearItemSlots()
                    setItems()
                }
                break
            }
            val id = 1001 + i
            val builder = ItemStackBuilder(Material.IRON_NUGGET, 1).setCustomModelData(id).setName(id.toString())
            if (model.modelOption.id == id) {
                builder.addGlowEffect()
            }
            val item = builder.build()
            setSlot(count % 18, builder.build()) { event ->
                model.modelOption.type = item.type
                model.modelOption.id = id
                addSelectGrowEffectToSingleItem(event)
            }
            count++
        }
        if (page > 1) {
            val back =
                ItemStackBuilder(Material.IRON_NUGGET, 1).setName(ChatColor.AQUA + "前のページ").setCustomModelData(17)
                    .build()
            setSlot(21, back) {
                if (page > 1) page--
                clearItemSlots()
                setItems()
            }
        }
    }
}