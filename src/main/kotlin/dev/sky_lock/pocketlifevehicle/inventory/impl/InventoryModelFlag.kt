package dev.sky_lock.pocketlifevehicle.inventory.impl

import dev.sky_lock.pocketlifevehicle.extension.chat.Line
import dev.sky_lock.pocketlifevehicle.inventory.InventoryCustom
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */

class InventoryModelFlag(private val player: Player, private val model: Model): InventoryCustom(18, "フラグ設定") {

    init {
        // 切り替え可能かなし
        val engineBell = ItemStackBuilder(Material.BELL, 1)
            .setName(Line().green("エンジン音"))
            .setLore(Line().yellow(model.flag.engineSoundText()))
            .build()
        setSlot(1, engineBell) { event ->
            model.flag.engineSound = !model.flag.engineSound
            event.currentItem = ItemStackBuilder(engineBell)
                .setLore(Line().yellow(model.flag.engineSoundText()))
                .build()
        }

        val animationDye = ItemStackBuilder(Material.YELLOW_DYE, 1)
            .setName(Line().green("ハンドリングのアニメーション"))
            .setLore(Line().yellow(model.flag.animationText()))
            .build()
        setSlot(3, animationDye) { event ->
            model.flag.animation = !model.flag.animation
            event.currentItem = ItemStackBuilder(animationDye)
                .setLore(Line().yellow(model.flag.animationText()))
                .build()
        }

        //消費する/消費しない
        val fuelCoal = ItemStackBuilder(Material.CHARCOAL, 1)
            .setName(Line().green("燃料消費"))
            .setLore(Line().yellow(model.flag.consumeFuelText()))
            .build()
        setSlot(5, fuelCoal) { event ->
            model.flag.consumeFuel = !model.flag.consumeFuel
            event.currentItem = ItemStackBuilder(fuelCoal)
                .setLore(Line().yellow(model.flag.consumeFuelText()))
                .build()
        }

        //専用/汎用
        val eventElytra = ItemStackBuilder(Material.ELYTRA, 1)
            .setName(Line().green("イベント仕様"))
            .setLore(Line().yellow(model.flag.eventOnlyText()))
            .build()
        setSlot(7, eventElytra) { event ->
            model.flag.eventOnly = !model.flag.eventOnly
            event.currentItem = ItemStackBuilder(eventElytra)
                .setLore(Line().yellow(model.flag.eventOnlyText()))
                .build()
        }

        val backBarrier = ItemStackBuilder(Material.BARRIER, 1).setName(Line().red("戻る")).build()
        setSlot(13, backBarrier) {
            player.openInventory(InventoryModelOption(player, model))
        }
    }
}