package dev.sky_lock.pocketlifevehicle.config

import dev.sky_lock.pocketlifevehicle.PLVehicle
import dev.sky_lock.pocketlifevehicle.vehicle.model.CollideBox
import dev.sky_lock.pocketlifevehicle.vehicle.model.ItemOption
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import dev.sky_lock.pocketlifevehicle.vehicle.model.Spec
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.configuration.serialization.ConfigurationSerialization
import java.nio.file.Path
import java.util.logging.Level

/**
 * @author sky_lock
 */

class ModelConfiguration {
    private val path: Path
    private var config: YamlConfiguration? = null

    init {
        ConfigurationSerialization.registerClass(Model::class.java)
        ConfigurationSerialization.registerClass(Spec::class.java)
        ConfigurationSerialization.registerClass(ItemOption::class.java)
        ConfigurationSerialization.registerClass(CollideBox::class.java)
        this.path = PLVehicle.getInstance().dataFolder.toPath().resolve("vehicles.yml")
    }

    fun loadModels(): MutableSet<Model> {
        config = BukkitConfiguration.load(path) ?: return HashSet()
        val listObj = config!!.getList("cars") as? List<Model> ?: return HashSet()
        val models: MutableSet<Model> = listObj.toMutableSet()
        models.removeAll(setOf<Any?>(null))
        return models
    }

    fun writeModels(models: Set<Model>) {
        if (config == null) {
            PLVehicle.getInstance().logger.log(Level.WARNING, "Could not write models to memory")
            return
        }
        config!!["cars"] = models.toList()
        config!![""]
    }

    fun saveToFile() {
        if (config == null) {
            PLVehicle.getInstance().logger.log(Level.WARNING, "Could not write models to file")
            return
        }
        BukkitConfiguration.save(path, config!!)
    }
}