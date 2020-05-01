package dev.sky_lock.pocketlifevehicle.config

import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import org.bukkit.configuration.file.YamlConfiguration
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.util.logging.Level

/**
 * @author sky_lock
 */
internal object BukkitConfiguration {
    private val logger = VehiclePlugin.instance.logger

    fun load(path: Path): YamlConfiguration? {
        var config: YamlConfiguration? = null
        createPath(path)
        try {
            Files.newBufferedReader(path, StandardCharsets.UTF_8).use {reader ->
                config = YamlConfiguration.loadConfiguration(reader)
                config!!.options().copyDefaults(true)
            }
        } catch (ex: IOException) {
            logger.log(Level.WARNING, "Could not load configurations from $path")
        }
        return config
    }

    fun save(path: Path, config: YamlConfiguration) {
        try {
            createPath(path)
            config.save(path.toFile())
        } catch (ex: IOException) {
            logger.log(Level.WARNING, "Could not save configurations to $path")
        }
    }

    private fun createPath(path: Path) {
        val dir = path.parent
        try {
            if (Files.isDirectory(dir) && Files.notExists(dir)) {
                Files.createDirectories(dir)
            }
            if (Files.notExists(path) && !Files.isReadable(path)) {
                Files.createFile(path)
            }
        } catch (ex: IOException) {
            logger.log(Level.WARNING, "Could not create directories or a file")
        }
    }
}