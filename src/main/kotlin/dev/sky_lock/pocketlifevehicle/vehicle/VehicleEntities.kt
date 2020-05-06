package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.extension.kotlin.truncateToOneDecimalPlace
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.json.ParkingViolation
import dev.sky_lock.pocketlifevehicle.vehicle.model.Capacity
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.*
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.persistence.PersistentDataType
import java.util.*

/**
 * @author sky_lock
 */
object VehicleEntities {
    private val entities: MutableMap<UUID, Vehicle> = HashMap()

    fun spawn(player: UUID, model: Model, location: Location, fuel: Float): Boolean {
        if (location.block.type != Material.AIR) {
            Bukkit.getPlayer(player)!!.sendActionBar(ChatColor.RED + "ブロックがあるので乗り物を設置できません")
            return false
        }
        return placeEntity(player, model, location, fuel)
    }

    fun placeEntity(player: UUID, model: Model, location: Location, fuel: Float): Boolean {
        var vehicle: Vehicle? = null
        vehicle = when (model.spec.capacity) {
            Capacity.ONE_SEAT -> {
                OneSeatVehicle(model)
            }
            Capacity.TWO_SEATS -> {
                TwoSeatsVehicle(model)
            }
            Capacity.FOR_SEATS -> {
                FourSeatsVehicle(model)
            }
        }
        vehicle.status.fuel = fuel
        vehicle.spawn(location)
        kill(player)
        entities[player] = vehicle
        return true
    }

    fun spawn(vehicle: Vehicle): Boolean {
        val owner = getOwner(vehicle) ?: return false
        return placeEntity(owner, vehicle.model, vehicle.location, vehicle.status.fuel)
    }

    fun kill(owner: UUID) {
        if (entities.containsKey(owner)) {
            val car = entities.remove(owner)
            car!!.remove()
        }
    }

    fun kill(vehicle: Vehicle) {
        if (entities.containsValue(vehicle)) {
            entities.values.remove(vehicle)
            vehicle.remove()
        }
    }

    fun tow(vehicle: Vehicle) {
        getOwner(vehicle)?.let { owner -> tow(owner, vehicle) }
    }

    fun tow(owner: UUID) {
        val car = entities[owner] ?: return
        tow(owner, car)
    }

    private fun tow(owner: UUID, vehicle: Vehicle) {
        val model = vehicle.model
        val itemStack = ItemStackBuilder(model.itemStack)
                .persistentData(VehiclePlugin.instance.createKey("owner"), PersistentDataType.STRING, owner.toString())
                .lore("所有者: " + getOwnerName(vehicle), "残燃料: " + vehicle.status.fuel.truncateToOneDecimalPlace())
                .itemFlags(*ItemFlag.values())
                .build()
        val location = vehicle.location
        location.world.dropItem(vehicle.location, itemStack)
        location.world.playSound(location, Sound.BLOCK_IRON_DOOR_OPEN, 1f, 0.2f)
        kill(owner)
    }

    fun getVehicle(armorStand: ArmorStand): Vehicle? {
        return entities.entries.find { entry -> entry.value.consistsOf(armorStand) }?.value
    }

    fun of(player: UUID): Vehicle? {
        return entities[player]
    }

    fun getOwner(vehicle: Vehicle): UUID? {
        return entities.entries.find { entry: Map.Entry<UUID, Vehicle> -> entry.value == vehicle }?.key
    }

    fun getOwnerName(vehicle: Vehicle): String {
        val uuid = getOwner(vehicle) ?: return "unknown"
        return Bukkit.getOfflinePlayer(uuid).name ?: "unknown"
    }

    fun scrapAll(modelId: String) {
        entities.values.filter { vehicle -> vehicle.model.id == modelId }
                .forEach { vehicle -> vehicle.isUndrivable = true }
    }

    fun registerIllegalParking(uuid: UUID) {
        val vehicle = of(uuid) ?: return
        val entry = ParkingViolation(Date(), uuid, vehicle.model.id, vehicle.status.fuel)
        VehiclePlugin.instance.parkingViolationList.registerNewEntry(entry)
        kill(uuid)
    }

    fun registerAllIllegalParkings() {
        entities.entries.removeIf { entry ->
            val vehicle = entry.value
            val parkingEntry = ParkingViolation(Date(), entry.key, vehicle.model.id, vehicle.status.fuel)
            VehiclePlugin.instance.parkingViolationList.registerNewEntry(parkingEntry)
            vehicle.remove()
            return@removeIf true
        }
    }

    fun respawn(player: Player): Boolean {
        val vehicle = of(player.uniqueId) ?: return false
        kill(player.uniqueId)
        return placeEntity(player.uniqueId, vehicle.model, player.location, vehicle.status.fuel)
    }

    fun restore(player: Player): Boolean {
        val plugin = VehiclePlugin.instance
        val entry = plugin.parkingViolationList.findEntry(player) ?: return false
        val model = Storage.MODEL.findById(entry.modelId)
        if (model == null) {
            plugin.parkingViolationList.removeEntry(player)
            return false
        }
        placeEntity(entry.ownerUuid, model, player.location, entry.fuel)
        plugin.parkingViolationList.removeEntry(player)
        return true
    }
}