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
import org.bukkit.util.Consumer

/**
 * @author sky_lock
 */
class FuelContents(player: Player) : MenuContents() {
    override fun onFlip(menu: InventoryMenu) {}
    private fun flipPage(player: Player, page: Int) {
        of(player).ifPresent { menu: InventoryMenu -> menu.flip(player, page) }
    }

    init {
        of(player.uniqueId).ifPresent { session: ModelOption ->
            var index = 1
            for (i in 0..5) {
                for (j in 0..8) {
                    if (j % 2 != 0) {
                        continue
                    }
                    val slot = 9 * i + j
                    val fuel = index * 25
                    val iron = ItemStackBuilder(Material.IRON_BLOCK, 1).name(fuel.toString()).build()
                    addSlot(Slot(slot, iron, Consumer {
                        session.maxFuel = fuel.toFloat()
                        flipPage(player, ModelMenuIndex.SETTING.ordinal)
                    }))
                    index += 1
                }
            }
        }
    }
}