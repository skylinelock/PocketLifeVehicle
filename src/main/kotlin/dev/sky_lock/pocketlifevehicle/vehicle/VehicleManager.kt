package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.extension.kotlin.truncateToOneDecimalPlace
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.item.UUIDTagType
import dev.sky_lock.pocketlifevehicle.json.ParkingViolation
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.*
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import java.util.*

/**
 * @author sky_lock
 */
object VehicleManager {
    private val vehicleMap: MutableMap<UUID, Vehicle> = HashMap()

    fun placeEntity(vehicle: Vehicle): Boolean {
        val playerUid = this.getOwnerUid(vehicle) ?: return false
        return this.placeEntity(playerUid, vehicle.model, vehicle.location, vehicle.state.fuel)
    }

    // 呼ぶ前に乗り物の設置が許可されているワールドか確認する
    fun placeEntity(playerUid: UUID, model: Model, location: Location, fuel: Float): Boolean {
        val vehicle = Vehicle(model)
        vehicle.state.fuel = fuel
        vehicle.spawn(location)
        this.kill(playerUid)
        vehicleMap[playerUid] = vehicle
        return true
    }

    fun kill(playerUid: UUID) {
        if (vehicleMap.containsKey(playerUid)) {
            val vehicle = vehicleMap.remove(playerUid) ?: return
            vehicle.remove()
        }
    }

    fun kill(vehicle: Vehicle) {
        if (vehicleMap.containsValue(vehicle)) {
            vehicleMap.values.remove(vehicle)
            vehicle.remove()
        }
    }

    fun pop(vehicle: Vehicle) {
        this.getOwnerUid(vehicle)?.let { playerUid -> this.pop(playerUid, vehicle) }
    }

    fun pop(playerUid: UUID) {
        val vehicle = vehicleMap[playerUid] ?: return
        this.pop(playerUid, vehicle)
    }

    private fun pop(playerUid: UUID, vehicle: Vehicle) {
        val model = vehicle.model
        val fuel = vehicle.state.fuel
        val itemStack = ItemStackBuilder(model.itemStack)
                .setPersistentData(VehiclePlugin.instance.createKey("owner"), UUIDTagType.INSTANCE, playerUid)
                .setPersistentData(VehiclePlugin.instance.createKey("fuel"), PersistentDataType.FLOAT, fuel)
                .addLore(
                        ChatColor.GREEN + "オーナー: " + ChatColor.YELLOW + getOwnerName(vehicle),
                        ChatColor.GREEN + "燃料: " + ChatColor.YELLOW + fuel.truncateToOneDecimalPlace()
                )
                .build()
        val location = vehicle.location
        location.world.dropItem(vehicle.location, itemStack)
        location.world.playSound(location, Sound.BLOCK_IRON_DOOR_OPEN, 1f, 0.2f)
        this.kill(playerUid)
    }

    fun getVehicle(armorStand: ArmorStand): Vehicle? {
        return vehicleMap.entries.find { entry -> entry.value.consistsOf(armorStand) }?.value
    }

    fun hasVehicle(playerUid: UUID): Boolean {
        return vehicleMap.containsKey(playerUid)
    }

    fun getLocation(playerUid: UUID): Location? {
        return vehicleMap[playerUid]?.location
    }

    fun getOwnerUid(vehicle: Vehicle): UUID? {
        return vehicleMap.entries.find { entry -> entry.value == vehicle }?.key
    }

    fun getOwnerName(vehicle: Vehicle): String {
        val uuid = getOwnerUid(vehicle) ?: return "unknown"
        return Bukkit.getOfflinePlayer(uuid).name ?: "unknown"
    }

    fun scrapAll(modelId: String) {
        vehicleMap.values.filter { vehicle -> vehicle.model.id == modelId }
                .forEach { vehicle -> vehicle.isUndrivable = true }
    }

    fun registerIllegalParking(playerUid: UUID) {
        val vehicle = vehicleMap[playerUid] ?: return
        val entry = ParkingViolation(Date(), playerUid, vehicle.model.id, vehicle.state.fuel)
        VehiclePlugin.instance.parkingViolationList.registerNewEntry(entry)
        this.kill(playerUid)
    }

    fun registerAllIllegalParkings() {
        vehicleMap.entries.removeIf { entry ->
            val vehicle = entry.value
            val parkingEntry = ParkingViolation(Date(), entry.key, vehicle.model.id, vehicle.state.fuel)
            VehiclePlugin.instance.parkingViolationList.registerNewEntry(parkingEntry)
            vehicle.remove()
            return@removeIf true
        }
    }

    fun verifyPlaceableLocation(location: Location): Boolean {
        return location.block.type == Material.AIR
    }

    fun respawn(player: Player): Boolean {
        val vehicle = vehicleMap[player.uniqueId] ?: return false
        val world = player.location.world ?: return false
        if (!VehiclePlugin.instance.pluginConfiguration.isWorldVehicleCanPlaced(world)) {
            return false
        }
        kill(player.uniqueId)
        return placeEntity(player.uniqueId, vehicle.model, player.location, vehicle.state.fuel)
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
        placeEntity(entry.ownerUuid, model, player.location, entry.fuel)
        plugin.parkingViolationList.removeEntry(player)
        return true
    }

    fun printLocation(player: Player) {

    }
}