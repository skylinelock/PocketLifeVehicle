package dev.sky_lock.pocketlifevehicle

// import games.pocketlife.play.VehicleAPI
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import dev.sky_lock.pocketlifevehicle.config.PluginConfiguration
import dev.sky_lock.pocketlifevehicle.inventory.CustomInventoryListener
import dev.sky_lock.pocketlifevehicle.json.ParkingViolationList
import dev.sky_lock.pocketlifevehicle.listener.ChunkEventListener
import dev.sky_lock.pocketlifevehicle.listener.PlayerEventListener
import dev.sky_lock.pocketlifevehicle.vehicle.ModelRegistry
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleManager
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author sky_lock
 */

class VehiclePlugin : JavaPlugin() {

    companion object {
        lateinit var instance: VehiclePlugin
    }

    lateinit var pluginConfiguration: PluginConfiguration
        private set
    lateinit var parkingViolationList: ParkingViolationList
        private set

    override fun onLoad() {
        CommandAPI.onLoad(CommandAPIBukkitConfig(this))

        VehicleEntityType.registerTypes()
    }

    override fun onEnable() {
        instance = this

        CommandAPI.onEnable()
        Command.register()

        this.pluginConfiguration = PluginConfiguration()
        this.parkingViolationList = ParkingViolationList()
        this.parkingViolationList.load()

        this.registerEventListeners()
        // VehicleAPI.registerImpl(VehicleAPIImpl())
    }

    override fun onDisable() {
        CommandAPI.onDisable()
        ModelRegistry.saveToFile()
        pluginConfiguration.save()

        VehicleManager.registerAllIllegalParkings()
        VehicleManager.removeEventVehicles()
        parkingViolationList.save()

        VehicleEntityType.unregisterTypes()
    }

    private fun registerEventListeners() {
        val pluginManager = server.pluginManager
        pluginManager.registerEvents(CustomInventoryListener(), this)
        pluginManager.registerEvents(ChunkEventListener(), this)
        pluginManager.registerEvents(PlayerEventListener(), this)
    }

}