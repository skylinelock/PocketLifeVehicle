package dev.sky_lock.pocketlifevehicle

import com.life.pocket.VehicleAPI
import dev.sky_lock.menu.InventoryMenuListener
import dev.sky_lock.pocketlifevehicle.command.CommandHandler
import dev.sky_lock.pocketlifevehicle.config.PluginConfiguration
import dev.sky_lock.pocketlifevehicle.json.ParkingViolationList
import dev.sky_lock.pocketlifevehicle.listener.ChunkEventListener
import dev.sky_lock.pocketlifevehicle.listener.InventoryEventListener
import dev.sky_lock.pocketlifevehicle.listener.PlayerEventListener
import dev.sky_lock.pocketlifevehicle.vehicle.Storage
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleEntities
import org.bstats.bukkit.Metrics
import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author sky_lock
 */

class VehiclePlugin : JavaPlugin() {

    companion object {
        lateinit var instance: VehiclePlugin
    }

    lateinit var pluginConfiguration: PluginConfiguration
    lateinit var parkingViolationList: ParkingViolationList

    override fun onEnable() {
        instance = this

        this.pluginConfiguration = PluginConfiguration()
        this.parkingViolationList = ParkingViolationList()
        this.parkingViolationList.load()

        getCommand("vehicle")?.setExecutor(CommandHandler())

        this.registerEventListeners()

        VehicleAPI.registerImpl(VehicleAPIImpl())

        Metrics(this, 7271)
    }

    override fun onDisable() {
        Storage.MODEL.saveToFile()
        pluginConfiguration.save()

        VehicleEntities.registerAllIllegalParkings()
        parkingViolationList.save()
    }

    private fun registerEventListeners() {
        val pluginManager = server.pluginManager
        pluginManager.registerEvents(InventoryMenuListener(this), this)
        pluginManager.registerEvents(ChunkEventListener(), this)
        pluginManager.registerEvents(PlayerEventListener(), this)
        pluginManager.registerEvents(InventoryEventListener(), this)
    }

    fun createKey(key: String): NamespacedKey {
        return NamespacedKey(this, key)
    }
}