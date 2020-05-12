package dev.sky_lock.pocketlifevehicle.gui.contents

import dev.sky_lock.menu.InventoryMenu
import dev.sky_lock.menu.InventoryMenu.Companion.of
import dev.sky_lock.menu.MenuContents
import dev.sky_lock.menu.Slot
import dev.sky_lock.pocketlifevehicle.gui.EditSessions.of
import dev.sky_lock.pocketlifevehicle.gui.ModelMenuIndex
import dev.sky_lock.pocketlifevehicle.gui.ModelOption
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.vehicle.model.Capacity
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */
class CapacityContents(player: Player) : MenuContents() {
    override fun onFlip(menu: InventoryMenu) {}
    private fun flipPage(player: Player, page: Int) {
        of(player).ifPresent { menu: InventoryMenu -> menu.flip(player, page) }
    }

    init {
        of(player.uniqueId).ifPresent { session: ModelOption ->
            addSlot(Slot(20, ItemStackBuilder(Material.OAK_PLANKS, 1).setName("1").build()) {
                session.capacity = Capacity.ONE_SEAT
                flipPage(player, ModelMenuIndex.SETTING.ordinal)
            })
            addSlot(Slot(22, ItemStackBuilder(Material.SPRUCE_PLANKS, 1).setName("2").build()) {
                session.capacity = Capacity.TWO_SEATS
                flipPage(player, ModelMenuIndex.SETTING.ordinal)
            })
            addSlot(Slot(24, ItemStackBuilder(Material.BIRCH_PLANKS, 1).setName("4").build()) {
                session.capacity = Capacity.FOR_SEATS
                flipPage(player, ModelMenuIndex.SETTING.ordinal)
            })
        }
    }
}