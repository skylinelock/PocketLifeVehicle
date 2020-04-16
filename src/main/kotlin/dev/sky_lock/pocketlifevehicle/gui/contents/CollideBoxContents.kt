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
import org.bukkit.inventory.ItemStack

/**
 * @author sky_lock
 */
class CollideBoxContents(private val player: Player) : MenuContents() {
    private val backItem = of(Material.ENDER_EYE, 1).name(ChatColor.RED.toString() + "戻る").build()
    private val baseSideItem = of(Material.LIGHT_BLUE_CONCRETE, 1).name("横").build()
    private val heightItem = of(Material.YELLOW_CONCRETE, 1).name("高さ").build()
    override fun onFlip(menu: InventoryMenu) {
        of(player.uniqueId).ifPresent { session: ModelOption ->
            if (session.collideBaseSide != 0.0f) {
                val growBaseSide: ItemStack = of(baseSideItem).glow().lore(session.collideBaseSide.toString()).build()
                updateItemStack(20, growBaseSide)
            }
            if (session.collideHeight != 0.0f) {
                val growHeight: ItemStack = of(heightItem).glow().lore(session.collideHeight.toString()).build()
                updateItemStack(24, growHeight)
            }
            menu.update()
        }
    }

    init {
        addSlot(Slot(4, backItem, org.bukkit.util.Consumer { of(player).ifPresent { menu: InventoryMenu -> menu.flip(player, ModelMenuIndex.SETTING.ordinal) } }))
        addSlot(Slot(20, baseSideItem, org.bukkit.util.Consumer { of(player).ifPresent { menu: InventoryMenu -> menu.flip(player, ModelMenuIndex.COLLIDE_BASESIDE.ordinal) } }))
        addSlot(Slot(24, heightItem, org.bukkit.util.Consumer { of(player).ifPresent { menu: InventoryMenu -> menu.flip(player, ModelMenuIndex.COLLIDE_HEIGHT.ordinal) } }))
    }
}