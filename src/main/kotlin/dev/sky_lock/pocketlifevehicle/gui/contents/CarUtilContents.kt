package dev.sky_lock.pocketlifevehicle.gui.contents

import dev.sky_lock.menu.InventoryMenu
import dev.sky_lock.menu.InventoryMenu.Companion.of
import dev.sky_lock.menu.MenuContents
import dev.sky_lock.menu.Slot
import dev.sky_lock.menu.ToggleSlot
import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.extension.kotlin.truncateToOneDecimalPlace
import dev.sky_lock.pocketlifevehicle.gui.CarUtilMenu
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.item.PlayerHeadBuilder
import dev.sky_lock.pocketlifevehicle.vehicle.Vehicle
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleEntities.getOwner
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleEntities.getOwnerName
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleEntities.tow
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Consumer
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * @author sky_lock
 */
class CarUtilContents(private val vehicle: Vehicle) : MenuContents() {
    private var refuelHopper: ItemStack
    override fun onFlip(menu: InventoryMenu) {
        refuelHopper = ItemStackBuilder(refuelHopper).lore(refuelInfo(vehicle.status.fuel)).build()
        updateItemStack(22, refuelHopper)

        val carInfoBook = ItemStackBuilder(Material.BOOK, 1).name(colorizeTitle("車両情報")).lore(carInfoLore()).build()
        this.removeSlot(24)
        this.addSlot(Slot(24, carInfoBook, org.bukkit.util.Consumer { }))

        menu.update()
        setFuelGage(menu)
    }

    private fun setFuelGage(menu: InventoryMenu) {
        val filled = ItemStackBuilder(Material.GREEN_STAINED_GLASS_PANE, 1).name(ChatColor.GREEN + "補充済み").build()
        val unfilled = ItemStackBuilder(Material.RED_STAINED_GLASS_PANE, 1).name(ChatColor.RED + "未補充").build()
        val fuel = vehicle.status.fuel
        val maxFuel = vehicle.model.spec.maxFuel
        val rate = fuel / maxFuel
        val filledSlots = (9 * rate).roundToInt()
        for (j in 0..8) {
            if (j < filledSlots) {
                menu.inventory.setItem(27 + j, filled)
                menu.inventory.setItem(36 + j, filled)
                menu.inventory.setItem(45 + j, filled)
            } else {
                menu.inventory.setItem(27 + j, unfilled)
                menu.inventory.setItem(36 + j, unfilled)
                menu.inventory.setItem(45 + j, unfilled)
            }
        }
    }

    private fun refuelInfo(fuel: Float): List<String> {
        return listOf(ChatColor.GRAY + "残燃料 : " + abs(fuel).truncateToOneDecimalPlace(), ChatColor.GRAY + "石炭ブロックを持って右クリック", ChatColor.GRAY + "すると燃料を補充できます")
    }

    private fun colorizeTitle(title: String): String {
        return ChatColor.GOLD + ChatColor.BOLD + title
    }

    private fun carInfoLore(): List<String> {
        val carInfo: MutableList<String> = ArrayList()
        carInfo.add(ChatColor.GREEN + "名前     : " + ChatColor.RESET + vehicle.model.name)
        carInfo.add(ChatColor.GREEN + "最大燃料 : " + ChatColor.RESET + vehicle.model.spec.maxFuel)
        carInfo.add(ChatColor.GREEN + "最高速度 : " + ChatColor.RESET + vehicle.model.spec.maxSpeed.label)
        carInfo.add(ChatColor.GREEN + "説明 :")
        vehicle.model.lore.forEach { lore -> carInfo.add("- " + ChatColor.RESET + lore) }
        if (vehicle.isUndrivable) {
            carInfo.add(ChatColor.GREEN + "状態 : " + ChatColor.RED + "廃車")
        } else {
            carInfo.add(ChatColor.GREEN + "状態 : " + ChatColor.YELLOW + "運転可能")
        }
        return carInfo
    }

    init {
        val closeItem = ItemStackBuilder(Material.ENDER_PEARL, 1).name(ChatColor.RED + "閉じる").build()
        val towItem = ItemStackBuilder(Material.MINECART, 1).name(colorizeTitle("回収する")).lore(ChatColor.GRAY + "アイテム化して持ち運べるようにします").build()

        val owner = getOwner(vehicle)
        val ownerSkull = PlayerHeadBuilder(1).owingPlayer(owner).name(colorizeTitle("所有者")).lore(ChatColor.AQUA + getOwnerName(vehicle)).build()
        val ownerSlot = Slot(20, ownerSkull, org.bukkit.util.Consumer { })

        val closeSlot = Slot(4, closeItem, org.bukkit.util.Consumer { event: InventoryClickEvent ->
            val menu = event.inventory.holder as CarUtilMenu?
            menu!!.close((event.whoClicked as Player))
        })
        val towSlot = Slot(11, towItem, org.bukkit.util.Consumer { event: InventoryClickEvent ->
            tow(vehicle)
            vehicle.closeMenu((event.whoClicked as Player))
        })
        val wield = ItemStackBuilder(Material.LIME_DYE, 1).name(ChatColor.RED + "" + ChatColor.BOLD + "ハンドリングのアニメーションを無効にする").build()
        val notWield = ItemStackBuilder(Material.MAGENTA_DYE, 1).name(ChatColor.GREEN + "" + ChatColor.BOLD + "ハンドリングのアニメーションを有効にする").build()
        val status = vehicle.status
        val wieldHandSlot: Slot = ToggleSlot(13, status.isWieldHand, wield, notWield, Consumer { status.isWieldHand = false }, Consumer { status.isWieldHand = true })
        val keyClose = ItemStackBuilder(Material.BARRIER, 1).name(ChatColor.RED + "" + ChatColor.BOLD + "鍵を閉める").build()
        val keyOpen = ItemStackBuilder(Material.STRUCTURE_VOID, 1).name(ChatColor.AQUA + "" + ChatColor.BOLD + "鍵を開ける").build()
        val keySlot: Slot = ToggleSlot(15, status.isLocked, keyOpen, keyClose, org.bukkit.util.Consumer { event: InventoryClickEvent ->
            status.isLocked = false
            val player = event.whoClicked as Player
            player.playSound(player.location, Sound.BLOCK_IRON_DOOR_OPEN, 1.0f, 1.4f)
        }, org.bukkit.util.Consumer { event: InventoryClickEvent ->
            status.isLocked = true
            val player = event.whoClicked as Player
            player.playSound(player.location, Sound.BLOCK_IRON_DOOR_CLOSE, 1.0f, 1.4f)
        })
        refuelHopper = ItemStackBuilder(Material.HOPPER, 1).name(colorizeTitle("給油口")).lore(refuelInfo(status.fuel)).build()
        val fuelSlot = Slot(22, refuelHopper, Consumer { event: InventoryClickEvent ->
            val cursor = event.cursor ?: return@Consumer
            if (cursor.type != Material.COAL_BLOCK) {
                return@Consumer
            }
            val success = vehicle.refuel(30f)
            if (success) {
                cursor.amount = cursor.amount - 1
                val player = event.whoClicked as Player
                player.playSound(player.location, Sound.BLOCK_BREWING_STAND_BREW, 1.0f, 0.6f)
                updateItemStack(22, ItemStackBuilder(refuelHopper).lore(refuelInfo(status.fuel)).build())
                of(player).ifPresent { menu: InventoryMenu ->
                    menu.update()
                    setFuelGage(menu)
                }
            }
        })
        super.addSlot(ownerSlot, closeSlot, wieldHandSlot, towSlot, keySlot, fuelSlot)
    }
}