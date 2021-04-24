package dev.sky_lock.pocketlifevehicle.inventory.impl

import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.extension.kotlin.truncateToOneDecimalPlace
import dev.sky_lock.pocketlifevehicle.inventory.InventoryCustom
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.item.PlayerHeadBuilder
import dev.sky_lock.pocketlifevehicle.vehicle.Vehicle
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleManager
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.ArrayList
import kotlin.math.abs
import kotlin.math.roundToInt


/**
 * @author sky_lock
 */

class InventoryVehicle(private val player: Player, private val vehicle: Vehicle) : InventoryCustom(54, "乗り物ユーティリティ") {

    init {
        val infoBook = ItemStackBuilder(Material.BOOK, 1).setName(colorizeTitle("車両情報")).setLore(vehicleInfoLore()).build()
        setItem(15, infoBook)

        val popCart = ItemStackBuilder(Material.MINECART, 1).setName(colorizeTitle("回収する"))
            .setLore(ChatColor.GRAY + "アイテム化して持ち運べるようにします").build()
        setSlot(2, popCart) {
            VehicleManager.pop(vehicle)
            player.closeInventory()
        }

        val owner = VehicleManager.getOwnerUid(vehicle)
        val ownerSkull = PlayerHeadBuilder(1).owingPlayer(owner).setName(colorizeTitle("所有者")).setLore(
            ChatColor.AQUA + VehicleManager.getOwnerName(
                vehicle
            )
        ).build()
        setItem(11, ownerSkull)

        val wieldDye = wieldDye()
        setSlot(4, wieldDye) { event ->
            val isWieldHand = vehicle.state.isWieldHand
            vehicle.state.isWieldHand = !isWieldHand
            event.currentItem = wieldDye()
        }

        setSlot(6, lockBarrier()) { event ->
            val isLocked = vehicle.state.isLocked
            if (isLocked) {
                player.playSound(player.location, Sound.BLOCK_IRON_DOOR_OPEN, 1.0f, 1.4f)
            } else {
                player.playSound(player.location, Sound.BLOCK_IRON_DOOR_CLOSE, 1.0f, 1.4f)
            }
            vehicle.state.isLocked = !isLocked
            event.currentItem = lockBarrier()
        }

        val refuelHopper =
            ItemStackBuilder(Material.HOPPER, 1).setName(colorizeTitle("給油口")).setLore(refuelInfo(vehicle.state.fuel))
                .build()
        setSlot(13, refuelHopper) { event ->
            val cursor = event.cursor ?: return@setSlot
            if (cursor.type != Material.COAL_BLOCK) return@setSlot
            if (!vehicle.refuel(30f)) return@setSlot

            cursor.amount -= 1
            player.playSound(player.location, Sound.BLOCK_BREWING_STAND_BREW, 1.0f, 0.6f)
            refuelHopper.lore = refuelInfo(vehicle.state.fuel)
            event.currentItem = refuelHopper
            updateFuelGage()
        }

        updateFuelGage()
    }

    private fun lockBarrier(): ItemStack {
        val lockDesc = listOf(ChatColor.GRAY + "他プレイヤーが乗り物に乗れるかどうか", ChatColor.GRAY + "を設定することができます")
        return if (vehicle.state.isLocked) {
            ItemStackBuilder(Material.BARRIER, 1).setName(ChatColor.AQUA + "" + ChatColor.BOLD + "鍵を開ける")
                .setLore(lockDesc).build()
        } else {
            ItemStackBuilder(Material.STRUCTURE_VOID, 1).setName(ChatColor.RED + "" + ChatColor.BOLD + "鍵を閉める")
                .setLore(lockDesc).build()
        }
    }

    private fun wieldDye(): ItemStack {
        return if (vehicle.state.isWieldHand) {
            ItemStackBuilder(
                Material.LIME_DYE,
                1
            ).setName(ChatColor.RED + "" + ChatColor.BOLD + "ハンドリングのアニメーションを無効にする").build()
        } else {
            ItemStackBuilder(
                Material.MAGENTA_DYE,
                1
            ).setName(ChatColor.GREEN + "" + ChatColor.BOLD + "ハンドリングのアニメーションを有効にする").build()
        }
    }

    private fun refuelInfo(fuel: Float): List<String> {
        return listOf(
            ChatColor.GRAY + "残燃料 : " + abs(fuel).truncateToOneDecimalPlace(),
            ChatColor.GRAY + "石炭ブロックを持って右クリック",
            ChatColor.GRAY + "すると燃料を補充できます"
        )
    }

    private fun colorizeTitle(title: String): String {
        return ChatColor.GOLD + ChatColor.BOLD + title
    }

    private fun updateFuelGage() {
        val filled = ItemStackBuilder(Material.GREEN_STAINED_GLASS_PANE, 1).setName(ChatColor.GREEN + "補充済み").build()
        val unfilled = ItemStackBuilder(Material.RED_STAINED_GLASS_PANE, 1).setName(ChatColor.RED + "未補充").build()
        val fuel = vehicle.state.fuel
        val maxFuel = vehicle.model.spec.maxFuel
        val rate = fuel / maxFuel
        val filledSlots = (9 * rate).roundToInt()
        for (j in 0..8) {
            if (j < filledSlots) {
                setItem(18 + j, filled)
                setItem(27 + j, filled)
                setItem(36 + j, filled)
                setItem(45 + j, filled)
            } else {
                setItem(18 + j, unfilled)
                setItem(27 + j, unfilled)
                setItem(36 + j, unfilled)
                setItem(45 + j, unfilled)
            }
        }
    }

    private fun vehicleInfoLore(): List<String> {
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

}
