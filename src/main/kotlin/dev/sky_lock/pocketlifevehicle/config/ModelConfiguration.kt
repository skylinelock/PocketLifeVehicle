package dev.sky_lock.pocketlifevehicle.config

import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import dev.sky_lock.pocketlifevehicle.vehicle.model.*
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
        ConfigurationSerialization.registerClass(Size::class.java)
        ConfigurationSerialization.registerClass(Flag::class.java)
        ConfigurationSerialization.registerClass(ModelOption::class.java)
        ConfigurationSerialization.registerClass(SeatOption::class.java)
        this.path = VehiclePlugin.instance.dataFolder.toPath().resolve("vehicles.yml")
    }

    fun loadModels(): MutableSet<Model> {
        config = BukkitConfiguration.load(path) ?: return HashSet()
        val carList = config.getList("vehicles")?.filterIsInstance<Model>() ?: return HashSet()
        return carList.toMutableSet()
    }

    fun writeModels(models: Set<Model>) {
        config["vehicles"] = models.toList()
        config[""]
    }

    fun saveToFile() {
        BukkitConfiguration.save(path, config)
    }
}