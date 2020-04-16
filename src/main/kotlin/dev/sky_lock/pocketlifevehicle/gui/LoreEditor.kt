package dev.sky_lock.pocketlifevehicle.gui

import dev.sky_lock.menu.InventoryMenu
import dev.sky_lock.menu.SignEditor
import dev.sky_lock.pocketlifevehicle.PLVehicle
import dev.sky_lock.pocketlifevehicle.packet.UpdateSignPacket
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors

/**
 * @author sky_lock
 */
class LoreEditor(player: Player) : SignEditor(PLVehicle.instance, player, Consumer { packet: UpdateSignPacket ->
    val lore = Arrays.stream(packet.lines).filter { line: String -> line.isNotEmpty() }.map { line: String? -> ChatColor.translateAlternateColorCodes('&', line!!) }.collect(Collectors.toList())
    EditSessions.of(player.uniqueId).ifPresent { session: ModelOption -> session.lore = lore }
    object : BukkitRunnable() {
        override fun run() {
            val inventoryMenu = player.openInventory.topInventory.holder as InventoryMenu?
            inventoryMenu!!.open(player, ModelMenuIndex.SETTING.ordinal)
        }
    }.runTaskLater(PLVehicle.instance, 1L)
}) 