package dev.sky_lock.pocketlifevehicle.gui.contents

import dev.sky_lock.menu.InventoryMenu
import dev.sky_lock.menu.InventoryMenu.Companion.of
import dev.sky_lock.menu.MenuContents
import dev.sky_lock.menu.Slot
import dev.sky_lock.pocketlifevehicle.gui.EditSessions.newSession
import dev.sky_lock.pocketlifevehicle.gui.EditSessions.of
import dev.sky_lock.pocketlifevehicle.gui.ModelMenuIndex
import dev.sky_lock.pocketlifevehicle.gui.ModelOption
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder.Companion.of
import dev.sky_lock.pocketlifevehicle.vehicle.Storage
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import java.util.*

/**
 * @author sky_lock
 */
class EditCarModelContents(player: Player) : MenuContents() {
    private val carItem = of(Material.CHEST_MINECART, 1).name(ChatColor.GREEN.toString() + "車種を追加する").build()
    override fun onFlip(menu: InventoryMenu) {}

    init {
        newSession(player.uniqueId)
        super.addSlot(Slot(4, of(Material.ENDER_EYE, 1).name(ChatColor.RED.toString() + "閉じる").build(), org.bukkit.util.Consumer { event: InventoryClickEvent -> of(player).ifPresent { menu: InventoryMenu -> menu.close((event.whoClicked as Player)) } }))
        var modelSlot = 9
        Storage.MODEL.forEach { model ->
            if (modelSlot > 44) {
                return@forEach
            }
            val desc: MutableList<String> = ArrayList()
            desc.add(ChatColor.DARK_AQUA.toString() + "名前: " + ChatColor.AQUA + model.name)
            //TODO: [] -> ""
            var lore: List<String?> = model.lore
            if (lore.isEmpty()) {
                lore = listOf("")
            }
            desc.add(ChatColor.DARK_AQUA.toString() + "説明: " + ChatColor.AQUA + lore)
            val spec = model.spec
            desc.add(ChatColor.DARK_AQUA.toString() + "燃料上限: " + ChatColor.AQUA + spec.maxFuel)
            desc.add(ChatColor.DARK_AQUA.toString() + "最高速度: " + ChatColor.AQUA + spec.maxSpeed.label)
            desc.add(ChatColor.DARK_AQUA.toString() + "乗車人数: " + ChatColor.AQUA + spec.capacity.value())
            val itemOption = model.itemOption
            val box = model.collideBox
            desc.add(ChatColor.DARK_AQUA.toString() + "モデル位置: " + ChatColor.AQUA + itemOption.position.label)
            desc.add(ChatColor.DARK_AQUA.toString() + "当たり判定: " + ChatColor.AQUA + box.baseSide + "×" + box.height)
            val size = if (model.isBig) "大きい" else "小さい"
            desc.add(ChatColor.DARK_AQUA.toString() + "大きさ: " + ChatColor.AQUA + size)
            desc.add(ChatColor.DARK_AQUA.toString() + "座高: " + ChatColor.AQUA + model.height)
            val item = of(model.itemStack).name(ChatColor.YELLOW.toString() + model.id).lore(desc).build()
            addSlot(Slot(modelSlot, item, org.bukkit.util.Consumer {
                of(player).ifPresent { menu: InventoryMenu ->
                    of(player.uniqueId).ifPresent { session: ModelOption ->
                        session.isJustEditing = true
                        session.id = model.id
                        session.name = model.name
                        session.lore = model.lore
                        session.maxFuel = spec.maxFuel
                        session.maxSpeed = spec.maxSpeed
                        session.capacity = spec.capacity
                        session.itemType = itemOption.type
                        session.setItemID(itemOption.id)
                        session.setItemPosition(itemOption.position)
                        session.collideBaseSide = model.collideBox.baseSide
                        session.collideHeight = model.collideBox.height
                        session.isBig = model.isBig
                        session.height = model.height
                        session.sound = model.sound
                        menu.flip(player, ModelMenuIndex.SETTING.ordinal)
                    }
                }
            }))
            modelSlot++
        }
        super.addSlot(Slot(49, carItem, org.bukkit.util.Consumer { event: InventoryClickEvent? ->
            of(player).ifPresent { menu: InventoryMenu ->
                of(player.uniqueId).ifPresent { session: ModelOption ->
                    session.isJustEditing = false
                    menu.flip(player, ModelMenuIndex.SETTING.ordinal)
                }
            }
        }))
    }
}