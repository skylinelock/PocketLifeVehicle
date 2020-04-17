package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.PLVehicle
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder.Companion.of
import dev.sky_lock.pocketlifevehicle.util.Formats
import dev.sky_lock.pocketlifevehicle.util.Profiles
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
object CarEntities {
    private val logger = PLVehicle.instance.logger
    private val entities: MutableMap<UUID, Car> = HashMap()

    fun spawn(player: UUID, model: Model, location: Location, fuel: Float): Boolean {
        if (location.block.type != Material.AIR) {
            Bukkit.getPlayer(player)!!.sendActionBar(ChatColor.RED.toString() + "ブロックがあるので車を設置できません")
            return false
        }
        var car: Car? = null
        val capacity = model.spec.capacity
        when {
            capacity === Capacity.ONE_SEAT -> {
                car = OneSeatCar(model)
            }
            capacity === Capacity.TWO_SEATS -> {
                car = TwoSeatsCar(model)
            }
            capacity === Capacity.FOR_SEATS -> {
                car = FourSeatsCar(model)
            }
        }
        if (car == null) {
            return false
        }
        car.status.fuel = fuel
        car.spawn(location)
        kill(player)
        entities[player] = car
        return true
    }

    fun spawn(car: Car): Boolean {
        val owner = getOwner(car) ?: return false
        return spawn(owner, car.model, car.location, car.status.fuel)
    }

    fun kill(owner: UUID) {
        if (entities.containsKey(owner)) {
            val car = entities.remove(owner)
            car!!.kill()
        }
    }

    fun kill(car: Car) {
        if (entities.containsValue(car)) {
            entities.values.remove(car)
            car.kill()
        }
    }

    private fun killAll() {
        entities.values.forEach(Consumer { obj: Car -> obj.kill() })
    }

    fun tow(car: Car) {
        getOwner(car)?.let { owner -> tow(owner, car) }
    }

    fun tow(owner: UUID) {
        val car = entities[owner] ?: return
        tow(owner, car)
    }

    private fun tow(owner: UUID, car: Car) {
        val model = car.model
        val itemStack = of(model.itemStack)
                .persistentData(PLVehicle.instance.createKey("owner"), PersistentDataType.STRING, owner.toString())
                .lore("所有者: " + Profiles.getName(owner), "残燃料: " + Formats.truncateToOneDecimalPlace(car.status.fuel))
                .itemFlags(*ItemFlag.values())
                .build()
        val location = car.location
        location.world.dropItem(car.location, itemStack)
        // item.setMetadata("mocar-fuel", new FixedMetadataValue(PLVehicle.getInstance(), car.getStatus().getFuel()));
        location.world.playSound(location, Sound.BLOCK_IRON_DOOR_OPEN, 1f, 0.2f)
        kill(owner)
    }

    fun getCar(seat: SeatArmorStand): Car? {
        return entities.values.find { car: Car -> car.contains(seat) }
    }

    fun getCar(basis: CarArmorStand): Car? {
        return entities.values.find { car: Car -> car.contains(basis) }
    }

    private val carEntities: Set<CarEntity>
        get() = entities.entries.map { entry -> CarEntity(entry.key.toString(), entry.value.model.id, entry.value.location, entry.value.status.fuel) }.toSet()

    fun of(player: UUID): Car? {
        return entities[player]
    }

    fun getOwner(car: Car): UUID? {
        return entities.entries.find { entry: Map.Entry<UUID, Car> -> entry.value == car }?.key
    }

    fun spawnAll() {
        try {
            PLVehicle.instance.entityStoreFile.load().forEach(Consumer { carEntity: CarEntity ->
                val model = Storage.MODEL.findById(carEntity.modelId)
                if (model != null) {
                    spawn(carEntity.owner, model, carEntity.location, carEntity.fuel)
                }
            })
        } catch (ex: IOException) {
            logger.warning("CarEntityの読み込みに失敗しました")
        }
    }

    fun saveAll() {
        try {
            PLVehicle.instance.entityStoreFile.save(carEntities)
            killAll()
        } catch (ex: IOException) {
            logger.warning("CarEntityの保存に失敗しました")
        }
    }
}