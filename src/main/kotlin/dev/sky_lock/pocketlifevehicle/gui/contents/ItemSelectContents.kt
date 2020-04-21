package dev.sky_lock.pocketlifevehicle.gui.contents

import dev.sky_lock.menu.InventoryMenu
import dev.sky_lock.menu.InventoryMenu.Companion.of
import dev.sky_lock.menu.MenuContents
import dev.sky_lock.menu.Slot
import dev.sky_lock.pocketlifevehicle.gui.EditSessions.of
import dev.sky_lock.pocketlifevehicle.gui.ModelMenuIndex
import dev.sky_lock.pocketlifevehicle.gui.ModelOption
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */
class ItemSelectContents(player: Player) : MenuContents() {
    private val VEHICLE_ITEM = Material.IRON_NUGGET
    override fun onFlip(menu: InventoryMenu) {}
    private fun flipPage(player: Player, page: Int) {
        of(player).ifPresent { menu: InventoryMenu -> menu.flip(player, page) }
    }

    init {
        val idOffset: Short = 1000
        var k: Short = 1
        for (i in 0..5) {
            for (j in 0..8) {
                val id = idOffset + k
                val item = ItemStackBuilder(VEHICLE_ITEM, 1).customModelData(id).build()
                addSlot(Slot(i * 9 + j, item, org.bukkit.util.Consumer {
                    of(player.uniqueId).ifPresent { session: ModelOption ->
                        session.itemType = VEHICLE_ITEM
                        session.setItemID(id)
                    }
                    flipPage(player, ModelMenuIndex.ITEM_OPTION.ordinal)
                }))
                k++
            }
        }
    }
}