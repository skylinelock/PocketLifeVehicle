package dev.sky_lock.pocketlifevehicle.gui.contents

import dev.sky_lock.menu.InventoryMenu
import dev.sky_lock.menu.InventoryMenu.Companion.of
import dev.sky_lock.menu.MenuContents
import dev.sky_lock.menu.Slot
import dev.sky_lock.pocketlifevehicle.gui.EditSessions.of
import dev.sky_lock.pocketlifevehicle.gui.ModelMenuIndex
import dev.sky_lock.pocketlifevehicle.gui.ModelOption
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder.Companion.of
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */
class CollideHeightContents(player: Player) : MenuContents() {
    override fun onFlip(inventoryMenu: InventoryMenu) {}

    init {
        for (i in 1..9) {
            val collideHeight = i * 0.5f
            val heightItem = of(Material.YELLOW_CONCRETE, 1).name(collideHeight.toString()).build()
            addSlot(Slot(17 + i, heightItem, org.bukkit.util.Consumer {
                of(player.uniqueId).ifPresent { session: ModelOption -> session.collideHeight = collideHeight }
                of(player).ifPresent { menu: InventoryMenu -> menu.flip(player, ModelMenuIndex.COLLIDE_BOX.ordinal) }
            }))
        }
    }
}