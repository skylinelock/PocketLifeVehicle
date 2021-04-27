package dev.sky_lock.pocketlifevehicle.inventory.impl

import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.inventory.InventoryCustom
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */

class InventoryModelFlag(private val player: Player, private val model: Model): InventoryCustom(18, "フラグ設定") {

    init {
        // 切り替え可能かなし
        val engineBell = ItemStackBuilder(Material.BELL, 1)
            .setName(ChatColor.GREEN + "エンジン音")
            .setLore(ChatColor.YELLOW + model.flag.engineSoundText())
            .build()
        setSlot(1, engineBell) { event ->
            model.flag.engineSound = !model.flag.engineSound
            event.currentItem = ItemStackBuilder(engineBell)
                .setLore(ChatColor.YELLOW + model.flag.engineSoundText())
                .build()
        }

        val animationDye = ItemStackBuilder(Material.YELLOW_DYE, 1)
            .setName(ChatColor.GREEN + "ハンドリングのアニメーション")
            .setLore(ChatColor.YELLOW + model.flag.animationText())
            .build()
        setSlot(3, animationDye) { event ->
            model.flag.animation = !model.flag.animation
            event.currentItem = ItemStackBuilder(animationDye)
                .setLore(ChatColor.YELLOW + model.flag.animationText())
                .build()
        }

        //消費する/消費しない
        val fuelCoal = ItemStackBuilder(Material.CHARCOAL, 1)
            .setName(ChatColor.GREEN + "燃料消費")
            .setLore(ChatColor.YELLOW + model.flag.consumeFuelText())
            .build()
        setSlot(5, fuelCoal) { event ->
            model.flag.consumeFuel = !model.flag.consumeFuel
            event.currentItem = ItemStackBuilder(fuelCoal)
                .setLore(ChatColor.YELLOW + model.flag.consumeFuelText())
                .build()
        }

        //専用/汎用
        val eventElytra = ItemStackBuilder(Material.ELYTRA, 1)
            .setName(ChatColor.GREEN + "イベント仕様")
            .setLore(ChatColor.YELLOW + model.flag.eventOnlyText())
            .build()
        setSlot(7, eventElytra) { event ->
            model.flag.eventOnly = !model.flag.eventOnly
            event.currentItem = ItemStackBuilder(eventElytra)
                .setLore(ChatColor.YELLOW + model.flag.eventOnlyText())
                .build()
        }

        val backBarrier = ItemStackBuilder(Material.BARRIER, 1).setName(ChatColor.RED + "戻る").build()
        setSlot(13, backBarrier) {
            player.openInventory(InventoryModelOption(player, model))
        }
    }
}