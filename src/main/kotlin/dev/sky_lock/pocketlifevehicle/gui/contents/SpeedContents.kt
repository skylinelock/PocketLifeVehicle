package dev.sky_lock.pocketlifevehicle.gui.contents

import dev.sky_lock.menu.InventoryMenu
import dev.sky_lock.menu.InventoryMenu.Companion.of
import dev.sky_lock.menu.MenuContents
import dev.sky_lock.menu.Slot
import dev.sky_lock.pocketlifevehicle.gui.EditSessions.of
import dev.sky_lock.pocketlifevehicle.gui.ModelMenuIndex
import dev.sky_lock.pocketlifevehicle.gui.ModelOption
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.vehicle.model.MaxSpeed
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * @author sky_lock
 */
class SpeedContents(player: Player) : MenuContents() {
    override fun onFlip(menu: InventoryMenu) {}
    private fun setSpeedAndReturn(player: Player, maxSpeed: MaxSpeed) {
        of(player.uniqueId).ifPresent { session: ModelOption ->
            session.maxSpeed = maxSpeed
            of(player).ifPresent { menu: InventoryMenu -> menu.flip(player, ModelMenuIndex.SETTING.ordinal) }
        }
    }

    init {
        val speedSelector = ItemStackBuilder(Material.SEA_LANTERN, 1).build()
        addSlot(Slot(11, ItemStackBuilder(speedSelector).name(MaxSpeed.SLOWEST.label).build(), org.bukkit.util.Consumer { setSpeedAndReturn(player, MaxSpeed.SLOWEST) }))
        addSlot(Slot(13, ItemStackBuilder(speedSelector).name(MaxSpeed.SLOW.label).build(), org.bukkit.util.Consumer { setSpeedAndReturn(player, MaxSpeed.SLOW) }))
        addSlot(Slot(15, ItemStackBuilder(speedSelector).name(MaxSpeed.NORMAL.label).build(), org.bukkit.util.Consumer { setSpeedAndReturn(player, MaxSpeed.NORMAL) }))
        addSlot(Slot(29, ItemStackBuilder(speedSelector).name(MaxSpeed.FAST.label).build(), org.bukkit.util.Consumer { setSpeedAndReturn(player, MaxSpeed.FAST) }))
        addSlot(Slot(31, ItemStackBuilder(speedSelector).name(MaxSpeed.FASTEST.label).build(), org.bukkit.util.Consumer { setSpeedAndReturn(player, MaxSpeed.FASTEST) }))
    }
}