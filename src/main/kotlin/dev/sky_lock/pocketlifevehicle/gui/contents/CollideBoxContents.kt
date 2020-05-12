package dev.sky_lock.pocketlifevehicle.gui.contents

import dev.sky_lock.menu.InventoryMenu
import dev.sky_lock.menu.InventoryMenu.Companion.of
import dev.sky_lock.menu.MenuContents
import dev.sky_lock.menu.Slot
import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.gui.EditSessions.of
import dev.sky_lock.pocketlifevehicle.gui.ModelMenuIndex
import dev.sky_lock.pocketlifevehicle.gui.ModelOption
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author sky_lock
 */
class CollideBoxContents(private val player: Player) : MenuContents() {
    private val backItem = ItemStackBuilder(Material.ENDER_EYE, 1).setName(ChatColor.RED + "戻る").build()
    private val baseSideItem = ItemStackBuilder(Material.LIGHT_BLUE_CONCRETE, 1).setName("底辺").build()
    private val heightItem = ItemStackBuilder(Material.YELLOW_CONCRETE, 1).setName("高さ").build()
    override fun onFlip(menu: InventoryMenu) {
        of(player.uniqueId).ifPresent { session: ModelOption ->
            if (session.collideBaseSide != 0.0f) {
                val growBaseSide: ItemStack = ItemStackBuilder(baseSideItem).addGlowEffect().setLore(session.collideBaseSide.toString()).build()
                updateItemStack(20, growBaseSide)
            }
            if (session.collideHeight != 0.0f) {
                val growHeight: ItemStack = ItemStackBuilder(heightItem).addGlowEffect().setLore(session.collideHeight.toString()).build()
                updateItemStack(24, growHeight)
            }
            menu.update()
        }
    }

    init {
        addSlot(Slot(4, backItem) { of(player).ifPresent { menu: InventoryMenu -> menu.flip(player, ModelMenuIndex.SETTING.ordinal) } })
        addSlot(Slot(20, baseSideItem) { of(player).ifPresent { menu: InventoryMenu -> menu.flip(player, ModelMenuIndex.COLLIDE_BASESIDE.ordinal) } })
        addSlot(Slot(24, heightItem) { of(player).ifPresent { menu: InventoryMenu -> menu.flip(player, ModelMenuIndex.COLLIDE_HEIGHT.ordinal) } })
    }
}