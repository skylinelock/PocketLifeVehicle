package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.config.ModelConfiguration
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * @author sky_lock
 */

class ModelStorage {
    private val config = ModelConfiguration();
    private val models = config.loadModels();

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

    fun forEach(action: (Model) -> Unit) {
        models.forEach(action)
    }

    //TODO
    fun size(): Int {
        return models.size
    }

    fun findByItemStack(itemStack: ItemStack): Model? {
        if (itemStack.type == Material.AIR || !itemStack.hasItemMeta()) {
            return null
        }
        val meta = itemStack.itemMeta
        val name = meta.displayName
        val itemId = meta.customModelData

        return models.find {model ->
            val modelName = model.name
            val itemOption = model.itemOption
            val modelItemId = itemOption.id
            val modelItemType = itemOption.type

            return@find itemStack.type == modelItemType && name == modelName && itemId == modelItemId
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