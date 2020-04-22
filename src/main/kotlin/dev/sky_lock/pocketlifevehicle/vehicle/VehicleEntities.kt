package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.PLVehicle
import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.extension.kotlin.truncateToOneDecimalPlace
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.vehicle.model.Capacity
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.*
import org.bukkit.inventory.ItemFlag
import org.bukkit.persistence.PersistentDataType
import java.io.IOException
import java.util.*
import java.util.function.Consumer

/**
 * @author sky_lock
 */
object VehicleEntities {
    private val logger = PLVehicle.instance.logger
    private val ENTITIES: MutableMap<UUID, Vehicle> = HashMap()

    fun spawn(player: UUID, model: Model, location: Location, fuel: Float): Boolean {
        if (location.block.type != Material.AIR) {
            Bukkit.getPlayer(player)!!.sendActionBar(ChatColor.RED + "ブロックがあるので乗り物を設置できません")
            return false
        }
        var vehicle: Vehicle? = null
        val capacity = model.spec.capacity
        when {
            capacity === Capacity.ONE_SEAT -> {
                vehicle = OneSeatVehicle(model)
            }
            capacity === Capacity.TWO_SEATS -> {
                vehicle = TwoSeatsVehicle(model)
            }
            capacity === Capacity.FOR_SEATS -> {
                vehicle = FourSeatsVehicle(model)
            }
        }
        if (vehicle == null) {
            return false
        }
        vehicle.status.fuel = fuel
        vehicle.spawn(location)
        kill(player)
        ENTITIES[player] = vehicle
        return true
    }

    fun spawn(vehicle: Vehicle): Boolean {
        val owner = getOwner(vehicle) ?: return false
        return spawn(owner, vehicle.model, vehicle.location, vehicle.status.fuel)
    }

    fun kill(owner: UUID) {
        if (ENTITIES.containsKey(owner)) {
            val car = ENTITIES.remove(owner)
            car!!.kill()
        }
    }

    fun kill(vehicle: Vehicle) {
        if (ENTITIES.containsValue(vehicle)) {
            ENTITIES.values.remove(vehicle)
            vehicle.kill()
        }
    }

    private fun killAll() {
        ENTITIES.values.forEach(Consumer { obj: Vehicle -> obj.kill() })
    }

    fun tow(vehicle: Vehicle) {
        getOwner(vehicle)?.let { owner -> tow(owner, vehicle) }
    }

    fun tow(owner: UUID) {
        val car = ENTITIES[owner] ?: return
        tow(owner, car)
    }

    private fun tow(owner: UUID, vehicle: Vehicle) {
        val model = vehicle.model
        val itemStack = ItemStackBuilder(model.itemStack)
                .persistentData(PLVehicle.instance.createKey("owner"), PersistentDataType.STRING, owner.toString())
                .lore("所有者: " + getOwnerName(vehicle), "残燃料: " + vehicle.status.fuel.truncateToOneDecimalPlace())
                .itemFlags(*ItemFlag.values())
                .build()
        val location = vehicle.location
        location.world.dropItem(vehicle.location, itemStack)
        // item.setMetadata("mocar-fuel", new FixedMetadataValue(PLVehicle.getInstance(), car.getStatus().getFuel()));
        location.world.playSound(location, Sound.BLOCK_IRON_DOOR_OPEN, 1f, 0.2f)
        kill(owner)
    }

    fun getCar(seat: SeatArmorStand): Vehicle? {
        return ENTITIES.values.find { vehicle: Vehicle -> vehicle.contains(seat) }
    }

    fun getCar(basis: ModelArmorStand): Vehicle? {
        return ENTITIES.values.find { vehicle: Vehicle -> vehicle.contains(basis) }
    }

    private val vehicleEntities: Set<VehicleEntity>
        get() = ENTITIES.entries.map { entry -> VehicleEntity(entry.key.toString(), entry.value.model.id, entry.value.location, entry.value.status.fuel) }.toSet()

    fun of(player: UUID): Vehicle? {
        return ENTITIES[player]
    }

    fun getOwner(vehicle: Vehicle): UUID? {
        return ENTITIES.entries.find { entry: Map.Entry<UUID, Vehicle> -> entry.value == vehicle }?.key
    }

    fun getOwnerName(vehicle: Vehicle): String {
        val uuid = getOwner(vehicle) ?: return "unknown"
        return Bukkit.getOfflinePlayer(uuid).name ?: "unknown"
    }

    fun spawnAll() {
        try {
            PLVehicle.instance.entityStoreFile.load().forEach(Consumer { vehicleEntity: VehicleEntity ->
                val model = Storage.MODEL.findById(vehicleEntity.modelId)
                if (model != null) {
                    spawn(vehicleEntity.owner, model, vehicleEntity.location, vehicleEntity.fuel)
                }
            })
        } catch (ex: IOException) {
            logger.warning("CarEntityの読み込みに失敗しました")
        }
    }

    fun saveAll() {
        try {
            PLVehicle.instance.entityStoreFile.save(vehicleEntities)
            killAll()
        } catch (ex: IOException) {
            logger.warning("CarEntityの保存に失敗しました")
        }
    }
}