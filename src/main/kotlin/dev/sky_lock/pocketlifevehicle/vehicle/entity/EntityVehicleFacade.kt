package dev.sky_lock.pocketlifevehicle.vehicle.entity

import dev.sky_lock.pocketlifevehicle.inventory.impl.InventoryEventVehicle
import dev.sky_lock.pocketlifevehicle.inventory.impl.InventoryVehicle
import dev.sky_lock.pocketlifevehicle.vehicle.entity.component.SeatArmorStand
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftArmorStand
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.*

class EntityVehicleFacade(private val bukkitEntity: CraftArmorStand, private val vehicle: EntityVehicle) {
    private val nmsEntity = bukkitEntity.handle

    fun getOwner(): UUID? {
        return vehicle.owner
    }

    fun getOwnerName(): String {
        return vehicle.ownerName
    }

    fun getYaw(): Float {
        return bukkitEntity.location.yaw
    }

    fun mount(player: Player) {
        VehicleManager.mountPlayer(bukkitEntity, player)
    }

    fun dismount() {
        VehicleManager.dismount(bukkitEntity)
    }

    fun isSeat(): Boolean {
        return nmsEntity is SeatArmorStand
    }

    fun isLocked(): Boolean {
        return vehicle.isLocked
    }

    fun isOccupied(): Boolean {
        return nmsEntity.isVehicle
    }

    fun hasReachedCapacity(player: Player): Boolean {
        return VehicleManager.isFull(player, vehicle)
    }

    fun showVehicleUtility(player: Player) {
        player.openInventory(InventoryVehicle(player, vehicle))
    }

    fun showEventVehicleUtility(player: Player) {
        player.openInventory(InventoryEventVehicle(player, vehicle))
    }

    companion object {
        fun fromBukkit(bukkitEntity: Entity): EntityVehicleFacade? {
            if (bukkitEntity !is CraftArmorStand) return null
            val vehicle = VehicleManager.findVehicle(bukkitEntity)
            return if (vehicle == null) null else EntityVehicleFacade(bukkitEntity, vehicle)
        }

    }

}