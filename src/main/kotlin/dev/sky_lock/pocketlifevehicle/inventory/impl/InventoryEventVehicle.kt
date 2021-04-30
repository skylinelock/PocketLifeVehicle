package dev.sky_lock.pocketlifevehicle.inventory.impl

import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.inventory.InventoryCustom
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.vehicle.Vehicle
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleManager
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author sky_lock
 */

class InventoryEventVehicle(private val player: Player, private val vehicle: Vehicle): InventoryCustom(9, "乗り物ユーティリティー(イベント)") {

    init {
        val deleteCart = ItemStackBuilder(Material.MINECART, 1).setName(ChatColor.RED + "この車両を削除する").build()
        setSlot(6, deleteCart) {
            VehicleManager.remove(vehicle)
            player.closeInventory()
        }

        val lockBarrier = lockBarrier()
        setSlot(4, lockBarrier) { event ->
            val isLocked = vehicle.isLocked
            if (isLocked) {
                player.playSound(player.location, Sound.BLOCK_IRON_DOOR_OPEN, 1.0f, 1.4f)
            } else {
                player.playSound(player.location, Sound.BLOCK_IRON_DOOR_CLOSE, 1.0f, 1.4f)
            }
            vehicle.isLocked = !isLocked
            event.currentItem = lockBarrier()
        }

        val infoBook = ItemStackBuilder(Material.BOOK, 1).setName(ChatColor.GREEN + "車両情報").setLore(vehicleInfoLore()).build()
        setItem(2, infoBook)
    }

    private fun vehicleInfoLore(): List<String> {
        val info: MutableList<String> = ArrayList()
        info.add(ChatColor.GREEN + "名前     : " + ChatColor.RESET + vehicle.model.name)
        info.add(ChatColor.GREEN + "最高速度 : " + ChatColor.RESET + vehicle.model.spec.maxSpeed.label)
        info.add(ChatColor.GREEN + "状態 : " + ChatColor.YELLOW + "イベント専用")
        return info
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
}