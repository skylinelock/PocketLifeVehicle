package dev.sky_lock.pocketlifevehicle.inventory.impl

import dev.sky_lock.pocketlifevehicle.ext.kotlin.truncateToOneDecimalPlace
import dev.sky_lock.pocketlifevehicle.inventory.InventoryCustom
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.item.PlayerHeadBuilder
import dev.sky_lock.pocketlifevehicle.text.Line
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleEffects.cancelEngineSound
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleManager
import dev.sky_lock.pocketlifevehicle.vehicle.entity.EntityVehicle
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import kotlin.math.abs
import kotlin.math.roundToInt


/**
 * @author sky_lock
 */

class InventoryVehicle(private val player: Player, private val vehicle: EntityVehicle)
    : InventoryCustom(54, "乗り物ユーティリティ") {

    init {
        val refuelHopper =
            ItemStackBuilder(Material.HOPPER, 1).setName(colorizeTitle("給油口")).setLore(refuelInfo(vehicle.fuel))
                .build()
        setSlot(2, refuelHopper) { event ->
            val cursor = event.cursor ?: return@setSlot
            if (cursor.type == Material.COAL_BLOCK) {
                if (!vehicle.refuel(30F)) return@setSlot
            } else if (cursor.type == Material.COAL) {
                if (!vehicle.refuel(3.0F)) return@setSlot
            } else {
                return@setSlot
            }

            cursor.amount -= 1
            player.playSound(player.location, Sound.BLOCK_BREWING_STAND_BREW, 1.0f, 0.6f)
            refuelHopper.lore(refuelInfo(vehicle.fuel).map { line -> line.toComponent() })
            event.currentItem = refuelHopper
            updateFuelGage()
        }

        if (vehicle.model.flag.engineSound) {
            val soundNote = soundNoteBlock()
            setSlot(8, soundNote) { event ->
                val shouldPlaySound = vehicle.shouldPlaySound
                vehicle.shouldPlaySound = !shouldPlaySound
                if (!vehicle.shouldPlaySound) cancelEngineSound(vehicle)
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
            .setLore(Line().gray("アイテム化して持ち運べるようにします")).build()
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
            Line().aqua(vehicle.ownerName)
        ).build()
        setItem(44, ownerSkull)

        val infoBook =
            ItemStackBuilder(Material.BOOK, 1).setName(colorizeTitle("車両情報")).setLore(vehicleInfoLore()).build()
        setItem(53, infoBook)

        updateFuelGage()
    }

    private fun lockBarrier(): ItemStack {
        val lockDesc = listOf(Line().gray("他プレイヤーが乗り物に乗れるかどうか"), Line().gray("を設定することができます"))
        return if (vehicle.isLocked) {
            ItemStackBuilder(Material.BARRIER, 1).setName(Line().aquaBold("鍵を開ける"))
                .setLore(lockDesc).build()
        } else {
            ItemStackBuilder(Material.STRUCTURE_VOID, 1).setName(Line().redBold("鍵を閉める"))
                .setLore(lockDesc).build()
        }
    }

    private fun soundNoteBlock(): ItemStack {
        return if (vehicle.shouldPlaySound) {
            ItemStackBuilder(
                Material.NOTE_BLOCK,
                1
            ).setName(Line().redBold("エンジン音を消す")).build()
        } else {
            ItemStackBuilder(
                Material.NOTE_BLOCK,
                1
            ).setName(Line().greenBold("エンジン音をつける")).build()
        }
    }

    private fun wieldDye(): ItemStack {
        return if (vehicle.shouldAnimate) {
            ItemStackBuilder(
                Material.LIME_DYE,
                1
            ).setName(Line().redBold("ハンドリングのアニメーションを無効にする")).build()
        } else {
            ItemStackBuilder(
                Material.MAGENTA_DYE,
                1
            ).setName(Line().greenBold("ハンドリングのアニメーションを有効にする")).build()
        }
    }

    private fun refuelInfo(fuel: Float): List<Line> {
        val currentFuel = abs(fuel).truncateToOneDecimalPlace()
        val maxFuel = abs(vehicle.model.spec.maxFuel).truncateToOneDecimalPlace()
        return listOf(
                Line().gray("残燃料 : $currentFuel/$maxFuel"),
                Line().gray("石炭ブロックを持って右クリック"),
                Line().gray("すると燃料を補充できます")
        )
    }

    private fun colorizeTitle(title: String): Line {
        return Line().goldBold(title)
    }

    private fun updateFuelGage() {
        val filled = ItemStackBuilder(Material.GREEN_STAINED_GLASS_PANE, 1).setName(Line().green("補充済み")).build()
        val unfilled = ItemStackBuilder(Material.RED_STAINED_GLASS_PANE, 1).setName(Line().red("未補充")).build()
        val fuel = vehicle.fuel
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

    private fun vehicleInfoLore(): List<Line> {
        val info: MutableList<Line> = ArrayList()
        info.add(Line().green("名前     : ").colorCoded(vehicle.model.name))
        info.add(Line().green("最大燃料 : ").white(vehicle.model.spec.maxFuel.toString()))
        info.add(Line().green("最高速度 : ").white(vehicle.model.spec.maxSpeed.label))
        info.add(Line().green("説明     :"))

        vehicle.model.lore.forEach { lore -> info.add(Line().darkGray("- ").colorCoded(lore)) }
        if (vehicle.isScrapped) {
            info.add(Line().green("状態 : ").red("廃車"))
        } else {
            info.add(Line().green("状態 : ").white("運転可能"))
        }
        return info
    }

}
