package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.config.ModelConfiguration
import dev.sky_lock.pocketlifevehicle.vehicle.model.*
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

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

    private fun checkIdEquality(model: Model, id: String): Boolean {
        return model.id.equals(id, ignoreCase = true)
    }

    fun findById(id: String): Model? {
        return models.find { model -> this.checkIdEquality(model, id) }
    }

    fun hasRegistered(id: String): Boolean {
        return models.any { model -> this.checkIdEquality(model, id) }
    }

    fun register(model: Model) {
        if (this.hasRegistered(model.id)) {
            return
        }
        models.add(model)
        this.config.writeModels(this.models)
    }

    fun unregister(id: String) {
        models.removeIf { model -> this.checkIdEquality(model, id) }
        this.config.writeModels(this.models)
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
        val itemId = meta.customModelData

        return models.find { model ->
            val modelOption = model.modelOption
            val modelItemId = modelOption.id
            val modelItemType = modelOption.type

            return@find itemStack.type == modelItemType && itemId == modelItemId
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