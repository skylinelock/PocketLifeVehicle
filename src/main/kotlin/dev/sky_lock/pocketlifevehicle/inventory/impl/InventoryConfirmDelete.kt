package dev.sky_lock.pocketlifevehicle.inventory.impl

import dev.sky_lock.pocketlifevehicle.extension.chat.Line
import dev.sky_lock.pocketlifevehicle.inventory.InventoryCustom
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * @author sky_lock
 */

class InventoryConfirmDelete(noFunc: (InventoryClickEvent) -> Unit, yesFunc: (InventoryClickEvent) -> Unit): InventoryCustom(27, "本当に削除しますか？") {

    init {
        val noWool = ItemStackBuilder(Material.RED_WOOL, 1).setName(Line().red("いいえ")).build()
        setSlot(12, noWool, noFunc)

        val yesWool = ItemStackBuilder(Material.GREEN_WOOL, 1).setName(Line().green("はい")).build()
        setSlot(14, yesWool, yesFunc)
    }

}