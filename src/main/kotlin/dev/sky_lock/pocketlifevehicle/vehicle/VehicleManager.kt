package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.extension.kotlin.truncateToOneDecimalPlace
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.item.UUIDTagType
import dev.sky_lock.pocketlifevehicle.json.ParkingViolation
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import java.util.*

/**
 * @author sky_lock
 */

object VehicleManager {
    private val vehicles = mutableListOf<Vehicle>()

    fun hasVehicle(uuid: UUID): Boolean {
        return findVehicle(uuid) != null
    }

    fun scrapAll(modelId: String) {
        vehicles.filter { vehicle -> vehicle.model.id == modelId }
            .forEach { vehicle -> vehicle.isUndrivable = true }
    }

    fun getLocation(owner: UUID): Location? {
        val vehicle = findVehicle(owner) ?: return null
        return vehicle.location
    }

    fun placeVehicle(vehicle: Vehicle) {
        vehicle.spawn(vehicle.location)
        vehicles.add(vehicle)
    }

    // 呼ぶ前に乗り物の設置が許可されているワールドか確認する
    fun placeVehicle(owner: UUID, location: Location, model: Model, fuel: Float): Boolean {
        val vehicle = Vehicle(owner, location, model, fuel)
        vehicle.spawn(location)
        remove(owner)
        vehicles.add(vehicle)
        return true
    }

    fun placeEventVehicle(location: Location, model: Model) {
        val vehicle = Vehicle(null, location, model, model.spec.maxFuel)
        vehicle.spawn(location)
        vehicles.add(vehicle)
    }

    fun setLockForEventVehicles(locked: Boolean) {
        vehicles.filter { vehicle -> vehicle.model.flag.eventOnly }
            .forEach { vehicle -> vehicle.isLocked = locked }
    }

    fun removeEventVehicles() {
        vehicles.filter { vehicle -> vehicle.model.flag.eventOnly }
            .forEach { vehicle -> remove(vehicle) }
    }

    fun pop(vehicle: Vehicle) {
        val model = vehicle.model
        val fuel = vehicle.tank.fuel
        val owner = vehicle.owner
        if (owner == null) {
            remove(vehicle)
            return
        }
        if (!vehicles.contains(vehicle)) {
            return
        }
        val itemStack = ItemStackBuilder(model.itemStack)
            .setPersistentData(VehiclePlugin.instance.createKey("owner"), UUIDTagType.INSTANCE, owner)
            .setPersistentData(VehiclePlugin.instance.createKey("fuel"), PersistentDataType.FLOAT, fuel)
            .addLore(
                ChatColor.GREEN + "オーナー: " + ChatColor.YELLOW + vehicle.ownerName,
                ChatColor.GREEN + "燃料: " + ChatColor.YELLOW + fuel.truncateToOneDecimalPlace()
            )
            .build()
        val location = vehicle.location
        location.world.dropItem(vehicle.location, itemStack)
        location.world.playSound(location, Sound.BLOCK_IRON_DOOR_OPEN, 1f, 0.2f)
        remove(owner)
    }

    fun pop(owner: UUID) {
        val vehicle = findVehicle(owner) ?: return
        pop(vehicle)
    }

    fun remove(uuid: UUID) {
        val vehicle = findVehicle(uuid) ?: return
        remove(vehicle)
    }

    fun remove(vehicle: Vehicle) {
        vehicle.remove()
        vehicles.remove(vehicle)
    }

    fun findVehicle(entity: ArmorStand): Vehicle? {
        return vehicles.find { vehicle -> vehicle.consistsOf(entity) }
    }

    private fun findVehicle(owner: UUID): Vehicle? {
        return vehicles.find{vehicle -> vehicle.owner != null && vehicle.owner == owner}
    }

    fun verifyPlaceableLocation(location: Location): Boolean {
        return location.block.type == Material.AIR
    }

    fun respawn(player: Player): Boolean {
        val vehicle = findVehicle(player.uniqueId) ?: return false
        val world = player.location.world ?: return false
        if (!VehiclePlugin.instance.pluginConfiguration.isWorldVehicleCanPlaced(world)) {
            return false
        }
        remove(player.uniqueId)
        return placeVehicle(player.uniqueId, player.location, vehicle.model, vehicle.tank.fuel)
    }

    fun restore(player: Player): Boolean {
        val plugin = VehiclePlugin.instance
        val entry = plugin.parkingViolationList.findEntry(player) ?: return false
        val model = ModelRegistry.findById(entry.modelId)
        if (model == null) {
            plugin.parkingViolationList.removeEntry(player)
            return false
        }
        val world = player.location.world ?: return false
        if (!VehiclePlugin.instance.pluginConfiguration.isWorldVehicleCanPlaced(world)) {
            return false
        }
        placeVehicle(entry.ownerUuid, player.location, model, entry.fuel)
        plugin.parkingViolationList.removeEntry(player)
        return true
    }

    fun registerIllegalParking(owner: UUID) {
        val vehicle = findVehicle(owner) ?: return
        val entry = ParkingViolation(Date(), owner, vehicle.model.id, vehicle.tank.fuel)
        VehiclePlugin.instance.parkingViolationList.registerNewEntry(entry)
        remove(owner)
    }

    fun registerAllIllegalParkings() {
        vehicles.removeIf { vehicle ->
            val owner = vehicle.owner ?: return@removeIf false
            val parkingEntry = ParkingViolation(Date(), owner, vehicle.model.id, vehicle.tank.fuel)
            VehiclePlugin.instance.parkingViolationList.registerNewEntry(parkingEntry)
            vehicle.remove()
            return@removeIf true
        }
    }
}