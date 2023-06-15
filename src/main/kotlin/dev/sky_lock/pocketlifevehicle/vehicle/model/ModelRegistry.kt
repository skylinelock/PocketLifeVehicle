package dev.sky_lock.pocketlifevehicle.vehicle.model

import dev.sky_lock.pocketlifevehicle.Keys
import dev.sky_lock.pocketlifevehicle.config.ModelConfiguration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

/**
 * @author sky_lock
 */

object ModelRegistry {

    private val config = ModelConfiguration()
    private val models = config.loadModels()

    fun default(id: String): Model {
        return Model(
            id = id,
            name = "name",
            lore = listOf(),
            spec = Spec(
                maxFuel = 450F,
                maxSpeed = MaxSpeed.NORMAL,
                steeringLevel = SteeringLevel.NORMAL
            ),
            flag = Flag(
                engineSound = true,
                animation = true,
                consumeFuel = true,
                eventOnly = false
            ),
            size = Size(
                baseSide = 1.0F,
                height = 1.0F
            ),
            height = 1.0F,
            sound = Sound.NONE,
            modelOption = ModelOption(
                type = Material.IRON_NUGGET,
                id = 1001,
                position = ItemPosition.HEAD,
                isBig = true
            ),
            seatOption = SeatOption(
                capacity = Capacity.QUAD,
                offset = 0.0F,
                depth = 1.0F,
                width = 1.0F
            )
        )
    }

    fun recreate(id: String, model: Model): Model {
        return Model(
            id = id,
            name = model.name,
            lore = model.lore,
            spec = model.spec,
            flag = model.flag,
            size = model.size,
            height = 1.0F,
            sound = Sound.NONE,
            modelOption = model.modelOption,
            seatOption = model.seatOption
        )
    }

    private fun checkIdEquality(model: Model, id: String): Boolean {
        return model.id.equals(id, ignoreCase = true)
    }

    fun findById(id: String): Model? {
        return models.find { model -> checkIdEquality(model, id) }
    }

    fun hasRegistered(id: String): Boolean {
        return models.any { model -> checkIdEquality(model, id) }
    }

    fun register(model: Model) {
        if (hasRegistered(model.id)) {
            return
        }
        models.add(model)
        config.writeModels(models)
    }

    fun unregister(id: String) {
        models.removeIf { model -> checkIdEquality(model, id) }
        config.writeModels(models)
    }

    fun set(): Set<Model> {
        return models.toSet()
    }

    fun findByItemStack(itemStack: ItemStack): Model? {
        if (itemStack.type == Material.AIR || !itemStack.hasItemMeta()) {
            return null
        }
        val meta = itemStack.itemMeta
        if (!meta.hasCustomModelData()) {
            return null
        }
        val container = meta.persistentDataContainer
        val id = container.get(Keys.MODEL_ID.namespace(), PersistentDataType.STRING) ?: return null
        val itemId = meta.customModelData

        return models.find { model ->
            val modelOption = model.modelOption
            val modelItemId = modelOption.id
            val modelItemType = modelOption.type

            return@find itemStack.type == modelItemType && itemId == modelItemId && model.id == id
        }
    }

    fun saveToFile() {
        config.saveToFile()
    }

    fun reloadConfig() {
        models.clear()
        models.addAll(config.loadModels())
    }
}