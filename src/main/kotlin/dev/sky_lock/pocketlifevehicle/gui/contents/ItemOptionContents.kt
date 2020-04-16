package dev.sky_lock.pocketlifevehicle.gui.contents

import dev.sky_lock.menu.InventoryMenu
import dev.sky_lock.menu.InventoryMenu.Companion.of
import dev.sky_lock.menu.MenuContents
import dev.sky_lock.menu.Slot
import dev.sky_lock.pocketlifevehicle.gui.EditSessions.of
import dev.sky_lock.pocketlifevehicle.gui.ModelMenuIndex
import dev.sky_lock.pocketlifevehicle.gui.ModelOption
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder.Companion.of
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * @author sky_lock
 */
class ItemOptionContents(private val player: Player) : MenuContents() {
    private val backItem = of(Material.ENDER_EYE, 1).name(ChatColor.RED.toString() + "戻る").build()
    private var itemItem = of(Material.MAGMA_CREAM, 1).name("アイテム").build()
    private var positionItem = of(Material.SLIME_BALL, 1).name("アイテム位置").build()
    override fun onFlip(menu: InventoryMenu) {
        of(player.uniqueId).ifPresent { session: ModelOption ->
            if (session.isItemValid) {
                itemItem = of(itemItem).glow().lore(session.itemType!!.name, session.itemId.toString()).build()
                updateItemStack(21, itemItem)
            }
            if (session.position != null) {
                positionItem = of(positionItem).glow().lore(session.position!!.label).build()
                updateItemStack(23, positionItem)
            }
            menu.update()
        }
    }

    init {
        addSlot(Slot(4, backItem, org.bukkit.util.Consumer { event: InventoryClickEvent? -> of(player).ifPresent { menu: InventoryMenu -> menu.flip(player, ModelMenuIndex.SETTING.ordinal) } }))
        addSlot(Slot(21, itemItem, org.bukkit.util.Consumer { event: InventoryClickEvent? -> of(player).ifPresent { menu: InventoryMenu -> menu.flip(player, ModelMenuIndex.ITEM_SELECT.ordinal) } }))
        addSlot(Slot(23, positionItem, org.bukkit.util.Consumer { event: InventoryClickEvent? -> of(player).ifPresent { menu: InventoryMenu -> menu.flip(player, ModelMenuIndex.ITEM_POS.ordinal) } }))
    }
}