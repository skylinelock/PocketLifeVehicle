package dev.sky_lock.pocketlifevehicle.vehicle.entity

import dev.sky_lock.pocketlifevehicle.Keys
import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import dev.sky_lock.pocketlifevehicle.ext.kotlin.truncateToOneDecimalPlace
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.json.ParkingViolation
import dev.sky_lock.pocketlifevehicle.nbt.CustomDataType
import dev.sky_lock.pocketlifevehicle.text.Line
import dev.sky_lock.pocketlifevehicle.util.Region
import dev.sky_lock.pocketlifevehicle.vehicle.entity.component.Components
import dev.sky_lock.pocketlifevehicle.vehicle.entity.component.ModelArmorStand
import dev.sky_lock.pocketlifevehicle.vehicle.entity.component.SeatArmorStand
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import dev.sky_lock.pocketlifevehicle.vehicle.model.ModelRegistry
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.persistence.PersistentDataType
import java.util.*

/**
 * @author sky_lock
 */

object VehicleManager {
    private val vehicles = mutableListOf<EntityVehicle>()

    fun findOrNull(uuid: UUID): EntityVehicle? {
        return vehicles.firstOrNull { it.uuid == uuid }
    }

    private fun findByOwner(owner: UUID): EntityVehicle? {
        return vehicles.find { it.owner == owner }
    }

    fun isOwner(owner: UUID): Boolean {
        return findByOwner(owner) != null
    }

    fun scrapAll(modelId: String) {
        vehicles.filter { it.model.id == modelId }
            .forEach { it.isScrapped = true }
    }

    fun getLocation(owner: UUID): Location? {
        val vehicle = findByOwner(owner) ?: return null
        return vehicle.location
    }

    private fun spawn(location: Location, model: Model, owner: UUID?, fuel: Float) {
        val world = location.world as CraftWorld
        val level = world.handle

        val vehicle = EntityVehicle(model, owner, location)
        vehicles.add(vehicle)

        val modelEntity = ModelArmorStand(Components.getModelEntityType(), level)

        vehicle.uuid = modelEntity.uuid
        vehicle.fuel = fuel

        world.addEntity<ArmorStand>(modelEntity, CreatureSpawnEvent.SpawnReason.CUSTOM)

        modelEntity.teleportTo(level, location.x, location.y, location.z, mutableSetOf(), location.yaw, location.pitch)
        modelEntity.entityVehicle = vehicle
        modelEntity.applyModelSettings()

        for (i in 0 until model.seatOption.capacity.value()) {
            val seatEntity = SeatArmorStand(Components.getSeatEntityType(), level)
            seatEntity.vehicleId = modelEntity.uuid
            seatEntity.entityVehicle = vehicle
            vehicle.registerSeat(seatEntity.uuid)
            world.addEntity<ArmorStand>(seatEntity, CreatureSpawnEvent.SpawnReason.CUSTOM)

            seatEntity.teleportTo(level, location.x, location.y, location.z, mutableSetOf(), location.yaw, location.pitch)
        }
    }

    // 呼ぶ前に乗り物の設置が許可されているワールドか確認する
    fun placeVehicle(owner: UUID, location: Location, model: Model, fuel: Float) {
        pop(owner)
        spawn(location, model, owner, fuel)
    }

    fun placeEventVehicle(location: Location, model: Model) {
        spawn(location, model, null, model.spec.maxFuel)
    }

    fun turnEventVehicleLockStatus(locked: Boolean) {
        vehicles.filter { it.isEventOnly() }
            .forEach { it.isLocked = locked }
    }

    fun removeEventVehicles() {
        vehicles.filter { it.isEventOnly() }
            .forEach { remove(it) }
    }

    fun removeEventVehicles(loc1: Location, loc2: Location) {
        vehicles.filter { it.isEventOnly() }
            .filter { Region(loc1, loc2).contains(it.location) }
            .forEach { remove(it) }
    }

    fun pop(vehicle: EntityVehicle) {
        val model = vehicle.model
        val fuel = vehicle.fuel
        val owner = vehicle.owner
        if (owner == null) {
            remove(vehicle)
            return
        }
        if (!vehicles.contains(vehicle)) {
            return
        }
        val itemStack = ItemStackBuilder(model.itemStack)
            .setPersistentData(Keys.OWNER.namespace(), CustomDataType.UUID, owner)
            .setPersistentData(Keys.FUEL.namespace(), PersistentDataType.FLOAT, fuel)
            .addLore(
                Line().green("オーナー: ").yellow(vehicle.ownerName),
                Line().green("燃料: ").yellow(fuel.truncateToOneDecimalPlace())
            )
            .build()
        val location = vehicle.location
        location.world.dropItem(vehicle.location, itemStack)
        location.world.playSound(location, Sound.BLOCK_IRON_DOOR_OPEN, 1f, 0.2f)
        remove(owner)
    }

    fun pop(owner: UUID) {
        val vehicle = findByOwner(owner) ?: return
        pop(vehicle)
    }

    fun remove(uuid: UUID) {
        val vehicle = findByOwner(uuid) ?: return
        remove(vehicle)
    }

    fun remove(vehicle: EntityVehicle) {
        kill(vehicle)
        vehicles.remove(vehicle)
    }

    fun findVehicle(entity: ArmorStand): EntityVehicle? {
        return vehicles.find { it -> it.uuid == entity.uniqueId || it.seats.any { it == entity.uniqueId } }
    }

    fun getSeatId(entity: ArmorStand): Int {
        val vehicle = findVehicle(entity) ?: return -1
        val seat = vehicle.seats.firstOrNull { it == entity.uniqueId } ?: return -1
        return vehicle.seats.indexOf(seat)
    }

    fun verifyPlaceableLocation(location: Location): Boolean {
        return location.block.type == Material.AIR
    }

    fun dismount(entity: ArmorStand) {
        val vehicle = findVehicle(entity) ?: return
        val driverSeatId = vehicle.seats[0]
        if (entity.uniqueId == driverSeatId) {
            vehicle.driver = null
        }
    }

    fun mountPlayer(entity: ArmorStand, player: Player) {
        val vehicle = findVehicle(entity) ?: return
        val nearByEntities = player.getNearbyEntities(2.0, 2.0, 2.0)

        if (entity.passengers.isNotEmpty()) return

        val modelSeatId = vehicle.uuid
        val driverSeatId = vehicle.seats[0]

        // クリックしたのがモデルだったら抽選
        if (entity.uniqueId == modelSeatId) {
            if (vehicle.driver == null) {
                val seat = nearByEntities.firstOrNull { it.uniqueId == driverSeatId } ?: return
                seat.addPassenger(player)
                vehicle.driver = player.uniqueId
                return
            }
            val seat = vehicle.seats.mapNotNull {
                    seatId -> nearByEntities.firstOrNull { it.uniqueId == seatId }
            }.filter { it.passengers.isEmpty() }.randomOrNull()
            seat?.addPassenger(player)
            return
        }
        if (entity.uniqueId == driverSeatId) {
            vehicle.driver = player.uniqueId
        }
        entity.addPassenger(player)
    }

    fun isFull(player: Player, vehicle: EntityVehicle): Boolean {
        if (vehicle.driver != null) return false
        val nearByEntities = player.getNearbyEntities(2.0, 2.0, 2.0)
        return vehicle.seats.mapNotNull { seatId ->
            nearByEntities.firstOrNull { it.uniqueId == seatId }
        }.none { it.passengers.isEmpty() }
    }

    fun respawn(player: Player): Boolean {
        val vehicle = findByOwner(player.uniqueId) ?: return false
        val world = player.location.world ?: return false
        if (!VehiclePlugin.instance.pluginConfiguration.isWorldVehicleCanPlaced(world)) {
            return false
        }
        remove(player.uniqueId)
        placeVehicle(player.uniqueId, player.location, vehicle.model, vehicle.fuel)
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
        val vehicle = findByOwner(owner) ?: return
        val entry = ParkingViolation(Date(), owner, vehicle.model.id, vehicle.fuel)
        VehiclePlugin.instance.parkingViolationList.registerNewEntry(entry)
        remove(owner)
    }

    fun unregisterIllegalParking(owner: UUID): Boolean {
        return VehiclePlugin.instance.parkingViolationList.removeEntry(owner)
    }

    fun unregisterDriver(player: UUID) {
        vehicles.filter { it.driver == player }.forEach { it.driver = null}
    }

    private fun kill(vehicle: EntityVehicle) {
        Bukkit.getWorlds().forEach { world ->
            world.entities.filter { entity -> entity.uniqueId == vehicle.uuid || vehicle.seats.any { seat -> entity.uniqueId == seat } }
                .forEach { entity -> entity.remove() }
        }
    }

    fun registerAllIllegalParkings() {
        vehicles.removeIf { vehicle ->
            val owner = vehicle.owner ?: return@removeIf false
            val parkingEntry = ParkingViolation(Date(), owner, vehicle.model.id, vehicle.fuel)
            VehiclePlugin.instance.parkingViolationList.registerNewEntry(parkingEntry)
            kill(vehicle)
            return@removeIf true
        }
    }

    fun getList(): List<EntityVehicle> {
        return vehicles.toList()
    }
}