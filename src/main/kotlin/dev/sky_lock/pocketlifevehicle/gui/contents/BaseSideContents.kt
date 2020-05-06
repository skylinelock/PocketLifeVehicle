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
class BaseSideContents(player: Player) : MenuContents() {
    override fun onFlip(menu: InventoryMenu) {}

    init {
        for (i in 1..9) {
            val baseSide = i * 0.5f
            val baseSideItem = ItemStackBuilder(Material.LIGHT_BLUE_CONCRETE, 1).name(baseSide.toString()).build()
            addSlot(Slot(17 + i, baseSideItem) {
                of(player.uniqueId).ifPresent { session: ModelOption -> session.collideBaseSide = baseSide }
                of(player).ifPresent { menu: InventoryMenu -> menu.flip(player, ModelMenuIndex.COLLIDE_BOX.ordinal) }
            })
        }
    }
}