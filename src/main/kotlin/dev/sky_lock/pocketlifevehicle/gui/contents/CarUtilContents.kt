package dev.sky_lock.pocketlifevehicle.gui.contents

import com.google.common.collect.ImmutableList
import dev.sky_lock.menu.InventoryMenu
import dev.sky_lock.menu.InventoryMenu.Companion.of
import dev.sky_lock.menu.MenuContents
import dev.sky_lock.menu.Slot
import dev.sky_lock.menu.ToggleSlot
import dev.sky_lock.pocketlifevehicle.gui.CarUtilMenu
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder.Companion.of
import dev.sky_lock.pocketlifevehicle.item.PlayerSkull.Companion.of
import dev.sky_lock.pocketlifevehicle.util.Formats.truncateToOneDecimalPlace
import dev.sky_lock.pocketlifevehicle.util.Profiles.getName
import dev.sky_lock.pocketlifevehicle.vehicle.Car
import dev.sky_lock.pocketlifevehicle.vehicle.CarEntities.getOwner
import dev.sky_lock.pocketlifevehicle.vehicle.CarEntities.tow
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Consumer
import java.util.*
import java.util.stream.Collectors
import kotlin.math.roundToInt

/**
 * @author sky_lock
 */
class CarUtilContents(private val car: Car) : MenuContents() {
    private var refuelHopper: ItemStack
    override fun onFlip(menu: InventoryMenu) {
        refuelHopper = of(refuelHopper).lore(refuelInfo(car.status.fuel)).build()
        updateItemStack(22, refuelHopper)
        menu.update()
        setFuelGage(menu)
    }

    private fun setFuelGage(menu: InventoryMenu) {
        val filled = of(Material.GREEN_STAINED_GLASS_PANE, 1).name(ChatColor.GREEN.toString() + "補充済み").build()
        val unFilled = of(Material.RED_STAINED_GLASS_PANE, 1).name(ChatColor.RED.toString() + "未補充").build()
        val fuel = car.status.fuel
        val maxFuel = car.model.spec.maxFuel
        val rate = fuel / maxFuel
        val filledSlots = (9 * rate).roundToInt()
        for (j in 0..8) {
            if (j < filledSlots) {
                menu.inventory.setItem(27 + j, filled)
                menu.inventory.setItem(36 + j, filled)
                menu.inventory.setItem(45 + j, filled)
            } else {
                menu.inventory.setItem(27 + j, unFilled)
                menu.inventory.setItem(36 + j, unFilled)
                menu.inventory.setItem(45 + j, unFilled)
            }
        }
    }

    private fun refuelInfo(fuel: Float): List<String> {
        return ImmutableList.of(ChatColor.GRAY.toString() + "残燃料 : " + truncateToOneDecimalPlace(Math.abs(fuel)), ChatColor.GRAY.toString() + "石炭ブロックを持って右クリック", ChatColor.GRAY.toString() + "すると燃料を補充できます")
    }

    private fun colorizeTitle(title: String): String {
        return ChatColor.GOLD.toString() + "" + ChatColor.BOLD + title
    }

    private fun colorizeInfoAsList(vararg lore: String): List<String> {
        return Arrays.stream(lore).map { l: String -> ChatColor.GRAY.toString() + l }.collect(Collectors.toList())
    }

    private fun colorizeContentAsLIst(vararg lore: String): List<String> {
        return Arrays.stream(lore).map { l: String -> ChatColor.AQUA.toString() + l }.collect(Collectors.toList())
    }

    private fun carInfoLore(): List<String> {
        val carInfo: MutableList<String> = ArrayList()
        carInfo.add(ChatColor.GREEN.toString() + "名前     : " + ChatColor.RESET + car.model.name)
        carInfo.add(ChatColor.GREEN.toString() + "最大燃料 : " + ChatColor.RESET + car.model.spec.maxFuel)
        carInfo.add(ChatColor.GREEN.toString() + "最高速度 : " + ChatColor.RESET + car.model.spec.maxSpeed.label)
        carInfo.add(ChatColor.GREEN.toString() + "説明 :")
        car.model.lore.forEach(java.util.function.Consumer { lore: String -> carInfo.add("- " + ChatColor.RESET + lore) })
        return carInfo
    }

    init {
        val closeItem = of(Material.ENDER_PEARL, 1).name(ChatColor.RED.toString() + "閉じる").build()
        val towItem = of(Material.MINECART, 1).name(colorizeTitle("レッカー移動")).lore(colorizeInfoAsList("アイテム化して持ち運べるようにします")).build()

        val owner = getOwner(car)
        val ownerSlot: Slot?

        ownerSlot = if (owner == null) {
            val unknownSkull = of(Material.PLAYER_HEAD, 1).name("unknown").build()
            Slot(20, unknownSkull)
        } else {
            val playerSkull = of(owner, 1).toItemStack()
            val ownerSkull = of(playerSkull).name(colorizeTitle("所有者")).lore(colorizeContentAsLIst(getName(owner)!!)).build()
            Slot(20, ownerSkull, org.bukkit.util.Consumer { })
        }

        val carInfoBook = of(Material.BOOK, 1).name(colorizeTitle("車両情報")).lore(carInfoLore()).build()
        val closeSlot = Slot(4, closeItem, org.bukkit.util.Consumer { event: InventoryClickEvent ->
            val menu = event.inventory.holder as CarUtilMenu?
            menu!!.close((event.whoClicked as Player))
        })
        val towSlot = Slot(11, towItem, org.bukkit.util.Consumer { event: InventoryClickEvent ->
            tow(car)
            car.closeMenu((event.whoClicked as Player))
        })
        val wield = of(Material.LIME_DYE, 1).name(ChatColor.RED.toString() + "" + ChatColor.BOLD + "ハンドリングのアニメーションを無効にする").build()
        val notWield = of(Material.MAGENTA_DYE, 1).name(ChatColor.GREEN.toString() + "" + ChatColor.BOLD + "ハンドリングのアニメーションを有効にする").build()
        val status = car.status
        val wieldHandSlot: Slot = ToggleSlot(13, status.isWieldHand, wield, notWield, Consumer { status.isWieldHand = false }, Consumer { status.isWieldHand = true })
        val carInfoSlot = Slot(24, carInfoBook, org.bukkit.util.Consumer { })
        val keyClose = of(Material.BARRIER, 1).name(ChatColor.RED.toString() + "" + ChatColor.BOLD + "鍵を閉める").build()
        val keyOpen = of(Material.STRUCTURE_VOID, 1).name(ChatColor.AQUA.toString() + "" + ChatColor.BOLD + "鍵を開ける").build()
        val keySlot: Slot = ToggleSlot(15, status.isLocked, keyOpen, keyClose, org.bukkit.util.Consumer { event: InventoryClickEvent ->
            status.isLocked = false
            val player = event.whoClicked as Player
            player.playSound(player.location, Sound.BLOCK_IRON_DOOR_OPEN, 1.0f, 1.4f)
        }, org.bukkit.util.Consumer { event: InventoryClickEvent ->
            status.isLocked = true
            val player = event.whoClicked as Player
            player.playSound(player.location, Sound.BLOCK_IRON_DOOR_CLOSE, 1.0f, 1.4f)
        })
        refuelHopper = of(Material.HOPPER, 1).name(colorizeTitle("給油口")).lore(refuelInfo(status.fuel)).build()
        val fuelSlot = Slot(22, refuelHopper, Consumer { event: InventoryClickEvent ->
            val cursor = event.cursor ?: return@Consumer
            if (cursor.type != Material.COAL_BLOCK) {
                return@Consumer
            }
            val success = car.refuel(30f)
            if (success) {
                cursor.amount = cursor.amount - 1
                val player = event.whoClicked as Player
                player.playSound(player.location, Sound.BLOCK_BREWING_STAND_BREW, 1.0f, 0.6f)
                updateItemStack(22, of(refuelHopper).lore(refuelInfo(status.fuel)).build())
                of(player).ifPresent { menu: InventoryMenu ->
                    menu.update()
                    setFuelGage(menu)
                }
            }
        })
        super.addSlot(ownerSlot, closeSlot, wieldHandSlot, towSlot, carInfoSlot, keySlot, fuelSlot)
    }
}