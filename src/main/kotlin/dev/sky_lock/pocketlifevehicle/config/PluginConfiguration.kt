package dev.sky_lock.pocketlifevehicle.config

import dev.sky_lock.pocketlifevehicle.PLVehicle
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.FileConfiguration
import java.io.File
import java.io.IOException

/**
 * @author sky_lock
 */

class PluginConfiguration {
    private val plugin = PLVehicle.instance
    private val config: FileConfiguration = plugin.config

    init {
        plugin.saveDefaultConfig()
        config.options().copyDefaults(true)
    }

    fun getAllowWorlds(): List<World?>  {
        val worlds: MutableList<World?> = ArrayList()
        config.getStringList("allow-worlds").filter { name ->
            Bukkit.getWorlds().any { world -> world.name.equals(name, ignoreCase = true) }
        }.map { name -> Bukkit.getWorld(name) }.toCollection(worlds)
        return worlds
    }

    fun getWarningCount(): Int {
        val count = config.getInt("warning-count")
        if (count < 1) {
            return 5
        }
        return count
    }

    fun addAllowWorld(name: String) {
        val allowWorlds = config.getStringList("allow-worlds")
        allowWorlds.add(name)
        config.set("allow-worlds", allowWorlds)
    }

    fun removeAllowWorld(name: String) {
        val allowWorlds = config.getStringList("allow-worlds")
        allowWorlds.remove(name)
        config.set("allow-worlds", allowWorlds)
    }

    fun save() {
        try {
            config.save(File(plugin.dataFolder, "config.yml"))
        } catch (ex: IOException) {
            plugin.logger.warning("Failed to save configurations")
        }
    }

    fun reload() {
        try {
            config.load(File(plugin.dataFolder, "config.yml"))
        } catch (ex: IOException) {
            plugin.logger.warning("Failed to load config.yml")
        } catch (ex: InvalidConfigurationException) {
            plugin.logger.warning("Failed to load config.yml")
        }
    }
}