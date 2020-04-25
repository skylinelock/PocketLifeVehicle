package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.PLVehicle
import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.extension.kotlin.truncateToOneDecimalPlace
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.vehicle.model.Capacity
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.*
import org.bukkit.entity.ArmorStand
import org.bukkit.inventory.ItemFlag
import org.bukkit.persistence.PersistentDataType
import java.util.*

/**
 * @author sky_lock
 */
object VehicleEntities {
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
            car!!.remove()
        }
    }

    fun kill(vehicle: Vehicle) {
        if (ENTITIES.containsValue(vehicle)) {
            ENTITIES.values.remove(vehicle)
            vehicle.remove()
        }
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

    fun getVehicle(armorStand: ArmorStand): Vehicle? {
        return ENTITIES.entries.find { entry -> entry.value.consistsOf(armorStand) }?.value
    }

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

    fun scrapAll(modelId: String) {
        ENTITIES.values.filter { vehicle -> vehicle.model.id == modelId }
                .forEach { vehicle -> vehicle.isUndrivable = true}
    }
}