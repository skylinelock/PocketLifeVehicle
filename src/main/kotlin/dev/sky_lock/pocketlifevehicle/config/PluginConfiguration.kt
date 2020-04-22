package dev.sky_lock.pocketlifevehicle.config

import dev.sky_lock.pocketlifevehicle.PLVehicle
import org.bukkit.World
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.FileConfiguration
import java.io.File
import java.io.IOException
import java.util.*

/**
 * @author sky_lock
 */

class PluginConfiguration {

    companion object {
        const val WORLDS_KEY = "allowed-world"
        const val COUNT_KEY = "warning-count"
    }
    private val plugin = PLVehicle.instance
    private val config: FileConfiguration = plugin.config

    init {

        plugin.saveDefaultConfig()
        config.options().copyDefaults(true)
    }

    private fun allowedWorlds(): List<UUID>  {
        return config.getStringList(WORLDS_KEY).map{str -> UUID.fromString(str)}.toMutableList()
    }

    fun warningCount(): Int {
        val count = config.getInt(COUNT_KEY)
        if (count < 1) {
            return 5
        }
        return count
    }

    fun isWorldVehicleCanPlaced(world: World): Boolean {
        return this.allowedWorlds().contains(world.uid)
    }

    fun setWorldVehicleCanPlaced(uuid: UUID, canBePlaced: Boolean) {
        val allowedWorlds = this.allowedWorlds().toMutableList()
        if (canBePlaced) allowedWorlds.add(uuid) else allowedWorlds.remove(uuid)
        config.set(WORLDS_KEY, allowedWorlds.map { uid -> uid.toString() })
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