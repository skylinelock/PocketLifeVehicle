package dev.sky_lock.pocketlifevehicle.gui.contents

import dev.sky_lock.menu.InventoryMenu
import dev.sky_lock.menu.InventoryMenu.Companion.of
import dev.sky_lock.menu.MenuContents
import dev.sky_lock.menu.Slot
import dev.sky_lock.pocketlifevehicle.gui.EditSessions.of
import dev.sky_lock.pocketlifevehicle.gui.ModelMenuIndex
import dev.sky_lock.pocketlifevehicle.gui.ModelOption
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder.Companion.of
import dev.sky_lock.pocketlifevehicle.vehicle.model.MaxSpeed
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * @author sky_lock
 */
class SpeedContents(player: Player) : MenuContents() {
    override fun onFlip(inventoryMenu: InventoryMenu) {}
    private fun setSpeedAndReturn(player: Player, maxSpeed: MaxSpeed) {
        of(player.uniqueId).ifPresent { session: ModelOption ->
            session.maxSpeed = maxSpeed
            of(player).ifPresent { menu: InventoryMenu -> menu.flip(player, ModelMenuIndex.SETTING.ordinal) }
        }
    }

    init {
        val speedSelector = of(Material.SEA_LANTERN, 1).build()
        addSlot(Slot(11, of(speedSelector).name(MaxSpeed.SLOWEST.label).build(), org.bukkit.util.Consumer { event: InventoryClickEvent? -> setSpeedAndReturn(player, MaxSpeed.SLOWEST) }))
        addSlot(Slot(13, of(speedSelector).name(MaxSpeed.SLOW.label).build(), org.bukkit.util.Consumer { event: InventoryClickEvent? -> setSpeedAndReturn(player, MaxSpeed.SLOW) }))
        addSlot(Slot(15, of(speedSelector).name(MaxSpeed.NORMAL.label).build(), org.bukkit.util.Consumer { event: InventoryClickEvent? -> setSpeedAndReturn(player, MaxSpeed.NORMAL) }))
        addSlot(Slot(29, of(speedSelector).name(MaxSpeed.FAST.label).build(), org.bukkit.util.Consumer { event: InventoryClickEvent? -> setSpeedAndReturn(player, MaxSpeed.FAST) }))
        addSlot(Slot(31, of(speedSelector).name(MaxSpeed.FASTEST.label).build(), org.bukkit.util.Consumer { event: InventoryClickEvent? -> setSpeedAndReturn(player, MaxSpeed.FASTEST) }))
    }
}