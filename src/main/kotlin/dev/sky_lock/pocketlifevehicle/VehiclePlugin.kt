package dev.sky_lock.pocketlifevehicle

// import games.pocketlife.play.VehicleAPI
import com.github.retrooper.packetevents.PacketEvents
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import dev.sky_lock.pocketlifevehicle.config.PluginConfiguration
import dev.sky_lock.pocketlifevehicle.inventory.CustomInventoryListener
import dev.sky_lock.pocketlifevehicle.inventory.LoreEditorListener
import dev.sky_lock.pocketlifevehicle.json.ParkingViolationList
import dev.sky_lock.pocketlifevehicle.listener.PlayerEventListener
import dev.sky_lock.pocketlifevehicle.vehicle.entity.VehicleManager
import dev.sky_lock.pocketlifevehicle.vehicle.entity.component.Components
import dev.sky_lock.pocketlifevehicle.vehicle.model.ModelRegistry
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder
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

        Components.registerEntityTypes()

        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this))
        PacketEvents.getAPI().getSettings().reEncodeByDefault(false)
            .checkForUpdates(true)
            .bStats(true);
        PacketEvents.getAPI().load();
    }

    override fun onEnable() {
        instance = this

        CommandAPI.onEnable()
        Command.register()

        PacketEvents.getAPI().eventManager.registerListener(LoreEditorListener())
        PacketEvents.getAPI().init();

        this.pluginConfiguration = PluginConfiguration()
        this.parkingViolationList = ParkingViolationList()
        this.parkingViolationList.load()

        this.registerEventListeners()
        // VehicleAPI.registerImpl(VehicleAPIImpl())
    }

    override fun onDisable() {
        CommandAPI.onDisable()
        PacketEvents.getAPI().terminate();

        ModelRegistry.saveToFile()
        pluginConfiguration.save()

        VehicleManager.registerAllIllegalParkings()
        VehicleManager.removeEventVehicles()
        parkingViolationList.save()
    }

    private fun registerEventListeners() {
        val pluginManager = server.pluginManager
        pluginManager.registerEvents(CustomInventoryListener(), this)
        pluginManager.registerEvents(PlayerEventListener(), this)
    }

}