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
import kotlin.math.abs
import kotlin.math.roundToInt


/**
 * @author sky_lock
 */

class InventoryVehicle(private val player: Player, private val vehicle: Vehicle)
    : InventoryCustom(54, "乗り物ユーティリティ") {

    init {
        val refuelHopper =
            ItemStackBuilder(Material.HOPPER, 1).setName(colorizeTitle("給油口")).setLore(refuelInfo(vehicle.tank.fuel))
                .build()
        setSlot(2, refuelHopper) { event ->
            val cursor = event.cursor ?: return@setSlot
            if (cursor.type == Material.COAL_BLOCK) {
                if (!vehicle.tank.refuel(30F)) return@setSlot
            } else if (cursor.type == Material.COAL) {
                if (!vehicle.tank.refuel(3.0F)) return@setSlot
            } else {
                return@setSlot
            }

            cursor.amount -= 1
            player.playSound(player.location, Sound.BLOCK_BREWING_STAND_BREW, 1.0f, 0.6f)
            refuelHopper.lore = refuelInfo(vehicle.tank.fuel)
            event.currentItem = refuelHopper
            updateFuelGage()
        }

        if (vehicle.model.flag.engineSound) {
            val soundNote = soundNoteBlock()
            setSlot(8, soundNote) { event ->
                val shouldPlaySound = vehicle.shouldPlaySound
                vehicle.shouldPlaySound = !shouldPlaySound
                if (vehicle.shouldPlaySound) {
                    vehicle.sound.start()
                } else {
                    vehicle.sound.stop()
                }
                event.currentItem = soundNoteBlock()
            }
        }

        if (vehicle.model.flag.animation) {
            val wieldDye = wieldDye()
            setSlot(17, wieldDye) { event ->
                val shouldAnimate = vehicle.shouldAnimate
                vehicle.shouldAnimate = !shouldAnimate
                event.currentItem = wieldDye()
            }
        }

        val popCart = ItemStackBuilder(Material.MINECART, 1).setName(colorizeTitle("回収する"))
            .setLore(ChatColor.GRAY + "アイテム化して持ち運べるようにします").build()
        setSlot(33, popCart) {
            VehicleManager.pop(vehicle)
            player.closeInventory()
        }

        setSlot(35, lockBarrier()) { event ->
            val isLocked = vehicle.isLocked
            if (isLocked) {
                player.playSound(player.location, Sound.BLOCK_IRON_DOOR_OPEN, 1.0f, 1.4f)
            } else {
                player.playSound(player.location, Sound.BLOCK_IRON_DOOR_CLOSE, 1.0f, 1.4f)
            }
            vehicle.isLocked = !isLocked
            event.currentItem = lockBarrier()
        }

        val ownerSkull = PlayerHeadBuilder(1).owingPlayer(vehicle.owner).setName(colorizeTitle("所有者")).setLore(
            ChatColor.AQUA + vehicle.getOwnerName()
        ).build()
        setItem(44, ownerSkull)

        val infoBook =
            ItemStackBuilder(Material.BOOK, 1).setName(colorizeTitle("車両情報")).setLore(vehicleInfoLore()).build()
        setItem(53, infoBook)

        updateFuelGage()
    }

    private fun lockBarrier(): ItemStack {
        val lockDesc = listOf(ChatColor.GRAY + "他プレイヤーが乗り物に乗れるかどうか", ChatColor.GRAY + "を設定することができます")
        return if (vehicle.isLocked) {
            ItemStackBuilder(Material.BARRIER, 1).setName(ChatColor.AQUA + "" + ChatColor.BOLD + "鍵を開ける")
                .setLore(lockDesc).build()
        } else {
            ItemStackBuilder(Material.STRUCTURE_VOID, 1).setName(ChatColor.RED + "" + ChatColor.BOLD + "鍵を閉める")
                .setLore(lockDesc).build()
        }
    }

    private fun soundNoteBlock(): ItemStack {
        return if (vehicle.shouldPlaySound) {
            ItemStackBuilder(
                Material.NOTE_BLOCK,
                1
            ).setName(ChatColor.RED + "" + ChatColor.BOLD + "エンジン音を消す").build()
        } else {
            ItemStackBuilder(
                Material.NOTE_BLOCK,
                1
            ).setName(ChatColor.GREEN + "" + ChatColor.BOLD + "エンジン音をつける").build()
        }
    }

    private fun wieldDye(): ItemStack {
        return if (vehicle.shouldAnimate) {
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
        val currentFuel = abs(fuel).truncateToOneDecimalPlace()
        val maxFuel = abs(vehicle.model.spec.maxFuel).truncateToOneDecimalPlace()
        return listOf(
            ChatColor.GRAY + "残燃料 : $currentFuel/$maxFuel",
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
        val fuel = vehicle.tank.fuel
        val maxFuel = vehicle.model.spec.maxFuel
        val rate = fuel / maxFuel
        val threshold = (5 * (1 - rate)).roundToInt()
        for (i in 0..4) {
            for (j in 0..4) {
                val slot = 9 * (j + 1) + i
                if (j < threshold) {
                    setItem(slot, unfilled)
                } else {
                    setItem(slot, filled)
                }
            }
        }
    }

    private fun vehicleInfoLore(): List<String> {
        val info: MutableList<String> = ArrayList()
        info.add(ChatColor.GREEN + "名前     : " + ChatColor.RESET + vehicle.model.name)
        info.add(ChatColor.GREEN + "最大燃料 : " + ChatColor.RESET + vehicle.model.spec.maxFuel)
        info.add(ChatColor.GREEN + "最高速度 : " + ChatColor.RESET + vehicle.model.spec.maxSpeed.label)
        info.add(ChatColor.GREEN + "説明 :")
        vehicle.model.lore.forEach { lore -> info.add("- " + ChatColor.RESET + lore) }
        if (vehicle.isUndrivable) {
            info.add(ChatColor.GREEN + "状態 : " + ChatColor.RED + "廃車")
        } else {
            info.add(ChatColor.GREEN + "状態 : " + ChatColor.YELLOW + "運転可能")
        }
        return info
    }

}
