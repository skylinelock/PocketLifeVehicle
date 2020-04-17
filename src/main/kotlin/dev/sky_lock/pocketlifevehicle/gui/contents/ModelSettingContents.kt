package dev.sky_lock.pocketlifevehicle.gui.contents

import dev.sky_lock.menu.InventoryMenu
import dev.sky_lock.menu.InventoryMenu.Companion.of
import dev.sky_lock.menu.MenuContents
import dev.sky_lock.menu.Slot
import dev.sky_lock.menu.ToggleSlot
import dev.sky_lock.pocketlifevehicle.PLVehicle
import dev.sky_lock.pocketlifevehicle.gui.EditSessions.destroy
import dev.sky_lock.pocketlifevehicle.gui.EditSessions.of
import dev.sky_lock.pocketlifevehicle.gui.LoreEditor
import dev.sky_lock.pocketlifevehicle.gui.ModelMenuIndex
import dev.sky_lock.pocketlifevehicle.gui.ModelSettingMenu
import dev.sky_lock.pocketlifevehicle.gui.StringEditor
import dev.sky_lock.pocketlifevehicle.gui.StringEditor.Companion.open
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder.Companion.of
import dev.sky_lock.pocketlifevehicle.vehicle.Storage
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.util.Consumer
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
    
    private var idItem = of(Material.EMERALD, 1).name("ID").build()
    private var nameItem = of(Material.NAME_TAG, 1).name("名前").build()
    private var loreItem = of(Material.OAK_SIGN, 1).name("説明").build()
    
    private var fuelItem = of(Material.COAL_BLOCK, 1).name("燃料上限").build()
    private var speedItem = of(Material.DIAMOND, 1).name("最高速度").build()
    private var capacityItem = of(Material.SADDLE, 1).name("乗車人数").build()
    private var steeringItem = of(Material.LEAD, 1).name("ステアリング").lore(ChatColor.RED.toString() + "Coming soon").build()

    private var itemOptionItem = of(Material.ITEM_FRAME, 1).name("アイテム").build()

    private var collideItem = of(Material.BEACON, 1).name(ChatColor.RESET.toString() + "当たり判定").build()
    private val standSmallItem = of(Material.ARMOR_STAND, 1).name("大きさ").lore("小さい").build()
    private val standBigItem = of(Material.ARMOR_STAND, 1).name("大きさ").lore("大きい").build()
    private var heightItem = of(Material.IRON_HORSE_ARMOR, 1).name("座高").build()
    private var soundItem = of(Material.BELL, 1).name("エンジン音").lore(ChatColor.RED.toString() + "Coming soon").build()

    private val updateItem = of(Material.END_CRYSTAL, 1).name(ChatColor.GREEN.toString() + "更新する").build()
    private val removeItem = of(Material.BARRIER, 1).name(ChatColor.RED.toString() + "削除する").build()
    private val createItem = of(Material.END_CRYSTAL, 1).name(ChatColor.GREEN.toString() + "追加する").build()
    
    init {
        of(player.uniqueId).ifPresent {
            addSlot(Slot(closeSlot.toInt(), of(Material.ENDER_EYE, 1).name(ChatColor.RED.toString() + "閉じる").build(), Consumer {
                of(player).ifPresent { menu: InventoryMenu -> menu.close(player) }
                destroy(player.uniqueId)
            }))
            addSlot(Slot(nameSlot.toInt(), nameItem, Consumer { of(player).ifPresent { menu: InventoryMenu? -> open(player, StringEditor.Type.NAME, menu as ModelSettingMenu) } }))
            addSlot(Slot(loreSlot.toInt(), loreItem, Consumer { LoreEditor(player).open() }))
            addSlot(Slot(fuelSlot.toInt(), fuelItem, Consumer { of(player).ifPresent { menu: InventoryMenu -> menu.flip(player, ModelMenuIndex.FUEL.ordinal) } }))
            addSlot(Slot(speedSlot.toInt(), speedItem, Consumer { of(player).ifPresent { menu: InventoryMenu -> menu.flip(player, ModelMenuIndex.SPEED.ordinal) } }))
            addSlot(Slot(capacitySlot.toInt(), capacityItem, Consumer { of(player).ifPresent { menu: InventoryMenu -> menu.flip(player, ModelMenuIndex.CAPACITY.ordinal) } }))
            addSlot(Slot(steeringSlot.toInt(), steeringItem, Consumer { }))
            addSlot(Slot(itemSlot.toInt(), itemOptionItem, Consumer { of(player).ifPresent { menu: InventoryMenu -> menu.flip(player, ModelMenuIndex.ITEM_OPTION.ordinal) } }))
            addSlot(Slot(collideSlot.toInt(), collideItem, Consumer { of(player).ifPresent { menu: InventoryMenu -> menu.flip(player, ModelMenuIndex.COLLIDE_BOX.ordinal) } }))
            addSlot(Slot(heightSlot.toInt(), heightItem, Consumer { of(player).ifPresent { menu: InventoryMenu? -> open(player, StringEditor.Type.HEIGHT, menu as ModelSettingMenu) } }))
            addSlot(Slot(soundSlot.toInt(), soundItem, Consumer { }))
        }
    }

    override fun onFlip(menu: InventoryMenu) {
        of(player.uniqueId).ifPresent { session ->
            if (session.isJustEditing) {
                removeSlot(removeSlot.toInt())
                addSlot(Slot(removeSlot.toInt(), removeItem, Consumer { event: InventoryClickEvent? ->
                    val id = session.id
                    if (id == null || id == "") {
                        return@Consumer
                    }
                    Storage.MODEL.unregister(id)
                    player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + id + "を削除しました")
                    destroy(player.uniqueId)
                    menu.close(player)
                }))
                removeSlot(makeSlot.toInt())
                addSlot(Slot(makeSlot.toInt(), updateItem, Consumer { event: InventoryClickEvent? ->
                    Storage.MODEL.unregister(session.id!!)
                    Storage.MODEL.register(session.generate())
                    player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + session.id + "を更新しました")
                    destroy(player.uniqueId)
                    menu.close(player)
                }))
            } else {
                removeSlot(idSlot.toInt())
                addSlot(Slot(idSlot.toInt(), idItem, Consumer { event: InventoryClickEvent? -> of(player).ifPresent { m: InventoryMenu? -> open(player, StringEditor.Type.ID, menu as ModelSettingMenu) } }))
                removeSlot(makeSlot.toInt())
                addSlot(Slot(makeSlot.toInt(), createItem, Consumer { event: InventoryClickEvent ->
                    val clicked = Objects.requireNonNull(event.currentItem)!!
                    if (!session.verifyCompleted()) {
                        val itemMeta = Objects.requireNonNull(clicked.itemMeta)
                        itemMeta.lore = session.unfilledOptionWarning()
                        clicked.itemMeta = itemMeta
                        event.currentItem = clicked
                        return@Consumer
                    }
                    if (Storage.MODEL.hasRegistered(session.id!!)) {
                        val itemMeta = Objects.requireNonNull(clicked.itemMeta)
                        itemMeta.lore = listOf(ChatColor.RED.toString() + "既に存在するIDです")
                        clicked.itemMeta = itemMeta
                        event.currentItem = clicked
                        return@Consumer
                    }
                    Storage.MODEL.register(session.generate())
                    player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "新しい車種を追加しました")
                    destroy(player.uniqueId)
                    player.closeInventory()
                }))
            }
            removeSlot(standSlot.toInt())
            addSlot(ToggleSlot(standSlot.toInt(), !session.isBig, standSmallItem, standBigItem, Consumer { event: InventoryClickEvent? -> session.isBig = true }, Consumer { event: InventoryClickEvent? -> session.isBig = false }))
            if (session.id != null && !session.id.equals("id", ignoreCase = true)) {
                idItem = of(idItem).lore(session.id!!).glow().build()
                updateItemStack(idSlot.toInt(), idItem)
            }
            if (session.name != null && !session.name.equals("name", ignoreCase = true)) {
                nameItem = of(nameItem).lore(session.name!!).glow().build()
                updateItemStack(nameSlot.toInt(), nameItem)
            }
            if (session.lore != null) {
                loreItem = of(loreItem).lore(session.lore!!).glow().build()
                updateItemStack(loreSlot.toInt(), loreItem)
            }
            if (session.maxFuel != 0.0f) {
                fuelItem = of(fuelItem).lore(session.maxFuel.toString()).glow().build()
                updateItemStack(fuelSlot.toInt(), fuelItem)
            }
            if (session.maxSpeed != null) {
                speedItem = of(speedItem).lore(session.maxSpeed!!.label).glow().build()
                updateItemStack(speedSlot.toInt(), speedItem)
            }
            if (session.capacity != null) {
                capacityItem = of(capacityItem).lore(session.capacity!!.value().toString()).glow().build()
                updateItemStack(capacitySlot.toInt(), capacityItem)
            }
            if (session.steeringLevel != null) {
                steeringItem = of(steeringItem).lore(session.steeringLevel!!.name).glow().build()
                updateItemStack(steeringSlot.toInt(), steeringItem)
            }
            if (session.isItemValid && session.position != null) {
                val modelId = session.itemId
                itemOptionItem = of(itemOptionItem).lore(session.itemType!!.name, modelId.toString(), session.position!!.label).glow().build()
                updateItemStack(itemSlot.toInt(), itemOptionItem)
            }
            if (session.collideHeight != 0.0f && session.collideBaseSide != 0.0f) {
                collideItem = of(collideItem).name(ChatColor.AQUA.toString() + "当たり判定").lore(session.collideBaseSide.toString(), session.collideHeight.toString()).glow().build()
                updateItemStack(collideSlot.toInt(), collideItem)
            }

            if (session.height != 0.0f) {
                heightItem = of(heightItem).lore(session.height.toString()).glow().build()
                updateItemStack(heightSlot.toInt(), heightItem)
            }
            if (session.sound != null) {
                soundItem = of(soundItem).lore(session.sound!!.name).build()
                updateItemStack(soundSlot.toInt(), soundItem)
            }

            menu.update()
        }
    }
}