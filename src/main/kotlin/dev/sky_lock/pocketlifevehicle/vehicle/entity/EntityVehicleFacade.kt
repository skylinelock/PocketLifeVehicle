package dev.sky_lock.pocketlifevehicle.vehicle.entity

import dev.sky_lock.pocketlifevehicle.inventory.impl.InventoryEventVehicle
import dev.sky_lock.pocketlifevehicle.inventory.impl.InventoryVehicle
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleManager
import dev.sky_lock.pocketlifevehicle.vehicle.entity.nms.ModelArmorStand
import dev.sky_lock.pocketlifevehicle.vehicle.entity.nms.SeatArmorStand
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftArmorStand
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.*

class EntityVehicleFacade(private val bukkitEntity: CraftArmorStand, private val vehicle: EntityVehicle) {
    private val nmsEntity = bukkitEntity.handle

    fun getOwner(): UUID? {
        return vehicle.status.owner
    }

    fun getOwnerName(): String {
        return vehicle.status.ownerName
    }

    fun getYaw(): Float {
        return bukkitEntity.location.yaw
    }

    fun mount(player: Player) {
        vehicle.addPassenger(player)
    }

    fun dismount(player: Player) {
        vehicle.ejectPassenger(player)
    }

    fun isSeat(): Boolean {
        return nmsEntity is SeatArmorStand
    }

    fun isDriverSeat(): Boolean {
        if (!isSeat()) return false
        return (nmsEntity as SeatArmorStand).isDriverSeat
    }

    fun isModel(): Boolean {
        return nmsEntity is ModelArmorStand
    }

    fun isLocked(): Boolean {
        return vehicle.status.isLocked
    }

    fun isOccupied(): Boolean {
        return nmsEntity.isVehicle
    }

    fun load() {
        if (vehicle.status.isLoaded) return
        vehicle.spawn()
        vehicle.status.isLoaded = true
    }

    fun unload() {
        if (!vehicle.status.isLoaded) return
        vehicle.remove()
        vehicle.status.isLoaded = false
    }

    fun hasReachedCapacity(): Boolean {
        return vehicle.passengers.size >= vehicle.status.model.seatOption.capacity.value()
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