package dev.sky_lock.pocketlifevehicle.config

import dev.sky_lock.pocketlifevehicle.PLVehicle
import dev.sky_lock.pocketlifevehicle.vehicle.model.CollideBox
import dev.sky_lock.pocketlifevehicle.vehicle.model.ItemOption
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import dev.sky_lock.pocketlifevehicle.vehicle.model.Spec
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.configuration.serialization.ConfigurationSerialization
import java.nio.file.Path

/**
 * @author sky_lock
 */

class ModelConfiguration {
    private val path: Path
    private lateinit var config: YamlConfiguration

    init {
        ConfigurationSerialization.registerClass(Model::class.java)
        ConfigurationSerialization.registerClass(Spec::class.java)
        ConfigurationSerialization.registerClass(ItemOption::class.java)
        ConfigurationSerialization.registerClass(CollideBox::class.java)
        this.path = PLVehicle.instance.dataFolder.toPath().resolve("vehicles.yml")
    }

    fun loadModels(): MutableSet<Model> {
        config = BukkitConfiguration.load(path) ?: return HashSet()
        val listObj = config.getList("cars") as? List<Model?> ?: return HashSet()
        return listObj.filterNotNull().toMutableSet()
    }

    fun writeModels(models: Set<Model>) {
        config["cars"] = models.toList()
        config[""]
    }

    fun saveToFile() {
        BukkitConfiguration.save(path, config)
    }
}