package dev.sky_lock.pocketlifevehicle.inventory.impl

import dev.sky_lock.pocketlifevehicle.inventory.InventoryCustom
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.text.Line
import dev.sky_lock.pocketlifevehicle.vehicle.EntityVehicle
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleManager
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author sky_lock
 */

class InventoryEventVehicle(private val player: Player, private val vehicle: EntityVehicle): InventoryCustom(9, "乗り物ユーティリティー(イベント)") {

    init {
        val deleteCart = ItemStackBuilder(Material.MINECART, 1).setName(Line().red("この車両を削除する")).build()
        setSlot(6, deleteCart) {
            VehicleManager.remove(vehicle)
            player.closeInventory()
        }

        val lockBarrier = lockBarrier()
        setSlot(4, lockBarrier) { event ->
            val isLocked = vehicle.status.isLocked
            if (isLocked) {
                player.playSound(player.location, Sound.BLOCK_IRON_DOOR_OPEN, 1.0f, 1.4f)
            } else {
                player.playSound(player.location, Sound.BLOCK_IRON_DOOR_CLOSE, 1.0f, 1.4f)
            }
            vehicle.status.isLocked = !isLocked
            event.currentItem = lockBarrier()
        }

        val infoBook = ItemStackBuilder(Material.BOOK, 1).setName(Line().green("車両情報")).setLore(*vehicleInfoLore().toTypedArray()).build()
        setItem(2, infoBook)
    }

    private fun vehicleInfoLore(): List<Line> {
        val info: MutableList<Line> = ArrayList()
        info.add(Line().green("名前     : ").colorCoded(vehicle.status.model.name))
        info.add(Line().green("最高速度 : ").raw(vehicle.status.model.spec.maxSpeed.label))
        info.add(Line().green("状態 : ").yellow("イベント専用"))
        return info
    }

    private fun lockBarrier(): ItemStack {
        val lockDesc = listOf(Line().gray("他プレイヤーが乗り物に乗れるかどうか"), Line().gray("を設定することができます"))
        return if (vehicle.status.isLocked) {
            ItemStackBuilder(Material.BARRIER, 1).setName(Line().aquaBold("鍵を開ける"))
                .setLore(lockDesc).build()
        } else {
            ItemStackBuilder(Material.STRUCTURE_VOID, 1).setName(Line().redBold("鍵を閉める"))
                .setLore(lockDesc).build()
        }
    }
}