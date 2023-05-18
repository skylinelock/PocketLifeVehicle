package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.PluginKey
import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import dev.sky_lock.pocketlifevehicle.extension.kotlin.truncateToOneDecimalPlace
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.item.UUIDTagType
import dev.sky_lock.pocketlifevehicle.json.ParkingViolation
import dev.sky_lock.pocketlifevehicle.text.Line
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
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
    private val vehicles = mutableListOf<EntityVehicle>()

    fun hasVehicle(uuid: UUID): Boolean {
        return findVehicle(uuid) != null
    }

    fun scrapAll(modelId: String) {
        vehicles.filter { vehicle -> vehicle.status.model.id == modelId }
                .forEach { vehicle -> vehicle.status.isUndrivable = true }
    }

    fun getLocation(owner: UUID): Location? {
        val vehicle = findVehicle(owner) ?: return null
        return vehicle.status.location
    }

    // 呼ぶ前に乗り物の設置が許可されているワールドか確認する
    fun placeVehicle(owner: UUID, location: Location, model: Model, fuel: Float) {
        val vehicle = EntityVehicle(owner, location, model, fuel)
        vehicle.spawn()
        pop(owner)
        vehicles.add(vehicle)
    }

    fun placeEventVehicle(location: Location, model: Model) {
        val vehicle = EntityVehicle(null, location, model, model.spec.maxFuel)
        vehicle.spawn()
        vehicles.add(vehicle)
    }

    fun setLockForEventVehicles(locked: Boolean) {
        vehicles.filter { vehicle -> vehicle.status.model.flag.eventOnly }
                .forEach { vehicle -> vehicle.status.isLocked = locked }
    }

    fun removeEventVehicles() {
        vehicles.filter { vehicle -> vehicle.status.model.flag.eventOnly }
                .forEach { vehicle -> remove(vehicle) }
    }

    fun pop(vehicle: EntityVehicle) {
        val model = vehicle.status.model
        val fuel = vehicle.status.tank.fuel
        val owner = vehicle.status.owner
        if (owner == null) {
            remove(vehicle)
            return
        }
        if (!vehicles.contains(vehicle)) {
            return
        }
        val itemStack = ItemStackBuilder(model.itemStack)
                .setPersistentData(PluginKey.OWNER, UUIDTagType.INSTANCE, owner)
                .setPersistentData(PluginKey.FUEL, PersistentDataType.FLOAT, fuel)
                .addLore(
                        Line().green("オーナー: ").yellow(vehicle.status.ownerName),
                        Line().green("燃料: ").yellow(fuel.truncateToOneDecimalPlace())
                )
        .build()
        val location = vehicle.status.location
        location.world.dropItem(vehicle.status.location, itemStack)
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

    fun remove(vehicle: EntityVehicle) {
        vehicle.remove()
        vehicles.remove(vehicle)
    }

    fun findVehicle(entity: ArmorStand): EntityVehicle? {
        return vehicles.find { vehicle -> vehicle.consistsOf(entity) }
    }

    private fun findVehicle(owner: UUID): EntityVehicle? {
        return vehicles.find { vehicle -> vehicle.status.owner != null && vehicle.status.owner == owner }
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
        placeVehicle(player.uniqueId, player.location, vehicle.status.model, vehicle.status.tank.fuel)
        return true
    }

    fun restore(player: Player): Boolean {
        val plugin = VehiclePlugin.instance
        val owner = player.uniqueId
        val entry = plugin.parkingViolationList.findEntry(owner) ?: return false
        val model = ModelRegistry.findById(entry.modelId)
        if (model == null) {
            plugin.parkingViolationList.removeEntry(owner)
            return false
        }
        val world = player.location.world ?: return false
        if (!VehiclePlugin.instance.pluginConfiguration.isWorldVehicleCanPlaced(world)) {
            return false
        }
        placeVehicle(entry.ownerUuid, player.location, model, entry.fuel)
        plugin.parkingViolationList.removeEntry(owner)
        return true
    }

    fun registerIllegalParking(owner: UUID) {
        val vehicle = findVehicle(owner) ?: return
        val entry = ParkingViolation(Date(), owner, vehicle.status.model.id, vehicle.status.tank.fuel)
        VehiclePlugin.instance.parkingViolationList.registerNewEntry(entry)
        remove(owner)
    }

    fun unregisterIllegalParking(owner: UUID): Boolean {
        return VehiclePlugin.instance.parkingViolationList.removeEntry(owner)
    }

    fun registerAllIllegalParkings() {
        vehicles.removeIf { vehicle ->
            val owner = vehicle.status.owner ?: return@removeIf false
            val parkingEntry = ParkingViolation(Date(), owner, vehicle.status.model.id, vehicle.status.tank.fuel)
            VehiclePlugin.instance.parkingViolationList.registerNewEntry(parkingEntry)
            vehicle.remove()
            return@removeIf true
        }
    }
}