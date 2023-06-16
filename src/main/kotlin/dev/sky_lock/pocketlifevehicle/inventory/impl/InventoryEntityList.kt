package dev.sky_lock.pocketlifevehicle.inventory.impl

import dev.sky_lock.pocketlifevehicle.inventory.InventoryCustom
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.text.Line
import dev.sky_lock.pocketlifevehicle.vehicle.entity.EntityVehicle
import dev.sky_lock.pocketlifevehicle.vehicle.entity.VehicleManager
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author sky_lock
 */

class InventoryEntityList(private val player: Player) : InventoryCustom(54, "エンティティ一覧") {

    init {
        setEntitySlots()
    }

    private fun setEntitySlots() {
        val vehicles = VehicleManager.getList()
        vehicles.forEachIndexed { index, vehicle ->
            setSlot(index, vehicleItem(vehicle)) {
                player.teleport(vehicle.location)
            }
        }
    }

    private fun vehicleItem(vehicle: EntityVehicle): ItemStack {
        val model = vehicle.model

        val desc = mutableListOf<Line>()
        val type = if (vehicle.isEventOnly()) "イベント車両" else "一般車両"
        desc.add(Line().darkAqua("タイプ: ").aqua(type))
        val owner = vehicle.owner
        if (owner != null) {
            desc.add(Line().darkAqua("オーナー: ").aqua(vehicle.ownerName))
        }
        val loc = vehicle.location
        desc.add(Line().darkAqua("位置: ").aqua("x=${loc.blockX}, y=${loc.blockY}, z=${loc.blockZ}"))

        return ItemStackBuilder(model.itemStack).setName(Line().yellow(model.name)).setLore(desc).build()
    }

}