package dev.sky_lock.pocketlifevehicle

import dev.sky_lock.menu.InventoryMenuListener
import dev.sky_lock.pocketlifevehicle.command.CommandHandler
import dev.sky_lock.pocketlifevehicle.config.PluginConfiguration
import dev.sky_lock.pocketlifevehicle.item.GlowEnchantment
import dev.sky_lock.pocketlifevehicle.json.EntityStoreFile
import dev.sky_lock.pocketlifevehicle.listener.ChunkEventListener
import dev.sky_lock.pocketlifevehicle.listener.InventoryEventListener
import dev.sky_lock.pocketlifevehicle.listener.PlayerEventListener
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleEntities
import dev.sky_lock.pocketlifevehicle.vehicle.Storage
import org.bstats.bukkit.Metrics
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author sky_lock
 */

class PLVehicle : JavaPlugin() {

    companion object {
        lateinit var instance: PLVehicle
    }

    lateinit var pluginConfiguration: PluginConfiguration
    val entityStoreFile: EntityStoreFile = EntityStoreFile(dataFolder.toPath())

    override fun onEnable() {
        instance = this

        pluginConfiguration = PluginConfiguration()

        val commandHandler = CommandHandler()
        getCommand("vehicle")?.setExecutor(commandHandler)

        this.registerEventListener()
        this.registerGlowEnchantment()

        VehicleEntities.spawnAll()

        Metrics(this, 7271)
    }

    override fun onDisable() {
        VehicleEntities.saveAll()
        Storage.MODEL.saveToFile()
        pluginConfiguration.save()
    }

    private fun registerEventListener() {
        val pluginManager = server.pluginManager
        pluginManager.registerEvents(InventoryMenuListener(this), this)
        pluginManager.registerEvents(ChunkEventListener(), this)
        pluginManager.registerEvents(PlayerEventListener(), this)
        pluginManager.registerEvents(InventoryEventListener(), this)
    }

    private fun registerGlowEnchantment() {
        try {
            val field = Enchantment::class.java.getDeclaredField("acceptingNew")
            field.isAccessible = true
            field.set(null, true)
        } catch (ex: NoSuchFileException) {
            logger.warning("Could not register the enchant for growing item")
        } catch (ex: IllegalAccessException) {
            logger.warning("Could not register the enchant for growing item")
        }
        Enchantment.registerEnchantment(GlowEnchantment())
    }

    fun createKey(key: String): NamespacedKey {
        return NamespacedKey(this, key)
    }
}