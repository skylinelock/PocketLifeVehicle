package dev.sky_lock.pocketlifevehicle.gui.contents

import dev.sky_lock.menu.InventoryMenu
import dev.sky_lock.menu.InventoryMenu.Companion.of
import dev.sky_lock.menu.MenuContents
import dev.sky_lock.menu.Slot
import dev.sky_lock.pocketlifevehicle.gui.EditSessions.of
import dev.sky_lock.pocketlifevehicle.gui.ModelMenuIndex
import dev.sky_lock.pocketlifevehicle.gui.ModelOption
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.vehicle.model.ItemPosition
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */
class ItemPositionContents(player: Player) : MenuContents() {
    override fun onFlip(menu: InventoryMenu) {}
    private fun setPositionAndReturn(player: Player, position: ItemPosition) {
        of(player.uniqueId).ifPresent { session: ModelOption ->
            session.setItemPosition(position)
            of(player).ifPresent { menu: InventoryMenu -> menu.flip(player, ModelMenuIndex.ITEM_OPTION.ordinal) }
        }
    }

    init {
        val positionSelector = ItemStackBuilder(Material.ARMOR_STAND, 1).build()
        addSlot(Slot(11, ItemStackBuilder(positionSelector).name(ItemPosition.HEAD.label).build(), org.bukkit.util.Consumer { setPositionAndReturn(player, ItemPosition.HEAD) }))
        addSlot(Slot(13, ItemStackBuilder(positionSelector).name(ItemPosition.HAND.label).build(), org.bukkit.util.Consumer { setPositionAndReturn(player, ItemPosition.HAND) }))
        addSlot(Slot(15, ItemStackBuilder(positionSelector).name(ItemPosition.CHEST.label).build(), org.bukkit.util.Consumer { setPositionAndReturn(player, ItemPosition.CHEST) }))
        addSlot(Slot(29, ItemStackBuilder(positionSelector).name(ItemPosition.FEET.label).build(), org.bukkit.util.Consumer { setPositionAndReturn(player, ItemPosition.FEET) }))
        addSlot(Slot(31, ItemStackBuilder(positionSelector).name(ItemPosition.LEGS.label).build(), org.bukkit.util.Consumer { setPositionAndReturn(player, ItemPosition.LEGS) }))
    }
}