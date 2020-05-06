package dev.sky_lock.pocketlifevehicle.gui.contents

import dev.sky_lock.menu.InventoryMenu
import dev.sky_lock.menu.InventoryMenu.Companion.of
import dev.sky_lock.menu.MenuContents
import dev.sky_lock.menu.Slot
import dev.sky_lock.menu.ToggleSlot
import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.extension.chat.sendPrefixedPluginMessage
import dev.sky_lock.pocketlifevehicle.gui.EditSessions.destroy
import dev.sky_lock.pocketlifevehicle.gui.EditSessions.of
import dev.sky_lock.pocketlifevehicle.gui.LoreEditor
import dev.sky_lock.pocketlifevehicle.gui.ModelMenuIndex
import dev.sky_lock.pocketlifevehicle.gui.ModelSettingMenu
import dev.sky_lock.pocketlifevehicle.gui.StringEditor
import dev.sky_lock.pocketlifevehicle.gui.StringEditor.Companion.open
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.vehicle.Storage
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleEntities
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import java.util.*

/**
 * @author sky_lock
 */

class ModelSettingContents(private val player: Player): MenuContents() {

    private val closeSlot: Short = 4
    private val idSlot: Short = 11
    private val nameSlot: Short = 13
    private val loreSlot: Short = 15
    private val fuelSlot: Short = 20
    private val speedSlot: Short = 22
    private val capacitySlot: Short = 24
    private val steeringSlot: Short = 29
    private val itemSlot: Short = 31
    private val collideSlot: Short = 33
    private val standSlot: Short = 38
    private val heightSlot: Short = 40
    private val soundSlot: Short = 42

    private val removeSlot: Short = 46
    private val makeSlot: Short = 49
    
    private var idItem = ItemStackBuilder(Material.EMERALD, 1).name("ID").build()
    private var nameItem = ItemStackBuilder(Material.NAME_TAG, 1).name("名前").build()
    private var loreItem = ItemStackBuilder(Material.OAK_SIGN, 1).name("説明").build()
    
    private var fuelItem = ItemStackBuilder(Material.COAL_BLOCK, 1).name("燃料上限").build()
    private var speedItem = ItemStackBuilder(Material.DIAMOND, 1).name("最高速度").build()
    private var capacityItem = ItemStackBuilder(Material.SADDLE, 1).name("乗車人数").build()
    private var steeringItem = ItemStackBuilder(Material.LEAD, 1).name("ステアリング").lore(ChatColor.RED + "Coming soon").build()

    private var itemOptionItem = ItemStackBuilder(Material.ITEM_FRAME, 1).name("アイテム").build()

    private var collideItem = ItemStackBuilder(Material.BEACON, 1).name(ChatColor.RESET + "当たり判定").build()
    private val standSmallItem = ItemStackBuilder(Material.ARMOR_STAND, 1).name("大きさ").lore("小さい").build()
    private val standBigItem = ItemStackBuilder(Material.ARMOR_STAND, 1).name("大きさ").lore("大きい").build()
    private var heightItem = ItemStackBuilder(Material.IRON_HORSE_ARMOR, 1).name("座高").build()
    private var soundItem = ItemStackBuilder(Material.BELL, 1).name("エンジン音").lore(ChatColor.RED + "Coming soon").build()

    private val updateItem = ItemStackBuilder(Material.END_CRYSTAL, 1).name(ChatColor.GREEN + "更新する").build()
    private val removeItem = ItemStackBuilder(Material.BARRIER, 1).name(ChatColor.RED + "削除する").build()
    private val createItem = ItemStackBuilder(Material.END_CRYSTAL, 1).name(ChatColor.GREEN + "追加する").build()
    
    init {
        of(player.uniqueId).ifPresent {
            addSlot(Slot(closeSlot.toInt(), ItemStackBuilder(Material.ENDER_EYE, 1).name(ChatColor.RED + "閉じる").build()) {
                of(player).ifPresent { menu -> menu.close(player) }
                destroy(player.uniqueId)
            })
            addSlot(Slot(nameSlot.toInt(), nameItem) { of(player).ifPresent { menu: InventoryMenu? -> open(player, StringEditor.Type.NAME, menu as ModelSettingMenu) } })
            addSlot(Slot(loreSlot.toInt(), loreItem) { LoreEditor(player).open() })
            addSlot(Slot(fuelSlot.toInt(), fuelItem) { of(player).ifPresent { menu: InventoryMenu -> menu.flip(player, ModelMenuIndex.FUEL.ordinal) } })
            addSlot(Slot(speedSlot.toInt(), speedItem) { of(player).ifPresent { menu: InventoryMenu -> menu.flip(player, ModelMenuIndex.SPEED.ordinal) } })
            addSlot(Slot(capacitySlot.toInt(), capacityItem) { of(player).ifPresent { menu: InventoryMenu -> menu.flip(player, ModelMenuIndex.CAPACITY.ordinal) } })
            addSlot(Slot(steeringSlot.toInt(), steeringItem))
            addSlot(Slot(itemSlot.toInt(), itemOptionItem) { of(player).ifPresent { menu: InventoryMenu -> menu.flip(player, ModelMenuIndex.ITEM_OPTION.ordinal) } })
            addSlot(Slot(collideSlot.toInt(), collideItem) { of(player).ifPresent { menu: InventoryMenu -> menu.flip(player, ModelMenuIndex.COLLIDE_BOX.ordinal) } })
            addSlot(Slot(heightSlot.toInt(), heightItem) { of(player).ifPresent { menu: InventoryMenu? -> open(player, StringEditor.Type.HEIGHT, menu as ModelSettingMenu) } })
            addSlot(Slot(soundSlot.toInt(), soundItem))
        }
    }

    override fun onFlip(menu: InventoryMenu) {
        of(player.uniqueId).ifPresent { session ->
            if (session.isJustEditing) {
                removeSlot(removeSlot.toInt())
                addSlot(Slot(removeSlot.toInt(), removeItem) onClick@{
                    val id = session.id
                    if (id == null || id == "") {
                        return@onClick
                    }
                    Storage.MODEL.unregister(id)
                    VehicleEntities.scrapAll(id)
                    player.sendPrefixedPluginMessage(ChatColor.GREEN + id + "を削除しました")
                    destroy(player.uniqueId)
                    menu.close(player)
                })
                removeSlot(makeSlot.toInt())
                addSlot(Slot(makeSlot.toInt(), updateItem) {
                    Storage.MODEL.unregister(session.id!!)
                    Storage.MODEL.register(session.generate())
                    player.sendPrefixedPluginMessage(ChatColor.GREEN + session.id!! + "を更新しました")
                    destroy(player.uniqueId)
                    menu.close(player)
                })
            } else {
                removeSlot(idSlot.toInt())
                addSlot(Slot(idSlot.toInt(), idItem) { of(player).ifPresent { open(player, StringEditor.Type.ID, menu as ModelSettingMenu) } })
                removeSlot(makeSlot.toInt())
                addSlot(Slot(makeSlot.toInt(), createItem) onClick@{ event: InventoryClickEvent ->
                    val clicked = Objects.requireNonNull(event.currentItem)!!
                    if (!session.verifyCompleted()) {
                        val itemMeta = Objects.requireNonNull(clicked.itemMeta)
                        itemMeta.lore = session.unfilledOptionWarning()
                        clicked.itemMeta = itemMeta
                        event.currentItem = clicked
                        return@onClick
                    }
                    if (Storage.MODEL.hasRegistered(session.id!!)) {
                        val itemMeta = Objects.requireNonNull(clicked.itemMeta)
                        itemMeta.lore = listOf(ChatColor.RED + "既に存在するIDです")
                        clicked.itemMeta = itemMeta
                        event.currentItem = clicked
                        return@onClick
                    }
                    Storage.MODEL.register(session.generate())
                    player.sendPrefixedPluginMessage(ChatColor.GREEN + "新しいモデルを追加しました")
                    destroy(player.uniqueId)
                    player.closeInventory()
                })
            }
            removeSlot(standSlot.toInt())
            addSlot(ToggleSlot(standSlot.toInt(), !session.isBig, standSmallItem, standBigItem,  { session.isBig = true },  { session.isBig = false }))
            if (session.id != null && !session.id.equals("id", ignoreCase = true)) {
                idItem = ItemStackBuilder(idItem).lore(session.id!!).glow().build()
                updateItemStack(idSlot.toInt(), idItem)
            }
            if (session.name != null && !session.name.equals("name", ignoreCase = true)) {
                nameItem = ItemStackBuilder(nameItem).lore(session.name!!).glow().build()
                updateItemStack(nameSlot.toInt(), nameItem)
            }
            if (session.lore != null) {
                loreItem = ItemStackBuilder(loreItem).lore(session.lore!!).glow().build()
                updateItemStack(loreSlot.toInt(), loreItem)
            }
            if (session.maxFuel != 0.0f) {
                fuelItem = ItemStackBuilder(fuelItem).lore(session.maxFuel.toString()).glow().build()
                updateItemStack(fuelSlot.toInt(), fuelItem)
            }
            if (session.maxSpeed != null) {
                speedItem = ItemStackBuilder(speedItem).lore(session.maxSpeed!!.label).glow().build()
                updateItemStack(speedSlot.toInt(), speedItem)
            }
            if (session.capacity != null) {
                capacityItem = ItemStackBuilder(capacityItem).lore(session.capacity!!.value().toString()).glow().build()
                updateItemStack(capacitySlot.toInt(), capacityItem)
            }
            if (session.steeringLevel != null) {
                steeringItem = ItemStackBuilder(steeringItem).lore(session.steeringLevel!!.name).glow().build()
                updateItemStack(steeringSlot.toInt(), steeringItem)
            }
            if (session.isItemValid && session.position != null) {
                val modelId = session.itemId
                itemOptionItem = ItemStackBuilder(itemOptionItem).lore(session.itemType!!.name, modelId.toString(), session.position!!.label).glow().build()
                updateItemStack(itemSlot.toInt(), itemOptionItem)
            }
            if (session.collideHeight != 0.0f && session.collideBaseSide != 0.0f) {
                collideItem = ItemStackBuilder(collideItem).name(ChatColor.AQUA + "当たり判定").lore("高さ: " + session.collideHeight.toString(), "底辺: " + session.collideBaseSide.toString()).glow().build()
                updateItemStack(collideSlot.toInt(), collideItem)
            }

            if (session.height != 0.0f) {
                heightItem = ItemStackBuilder(heightItem).lore(session.height.toString()).glow().build()
                updateItemStack(heightSlot.toInt(), heightItem)
            }
            if (session.sound != null) {
                soundItem = ItemStackBuilder(soundItem).lore(session.sound!!.name).build()
                updateItemStack(soundSlot.toInt(), soundItem)
            }

            menu.update()
        }
    }
}