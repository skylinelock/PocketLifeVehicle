package dev.sky_lock.mocar;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import dev.sky_lock.mocar.car.Cars;
import dev.sky_lock.mocar.listener.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author sky_lock
 */

public class MoCar extends JavaPlugin {

    private static MoCar instance;
    private ProtocolManager protocolManager;
    private Cars cars;

    @Override
    public void onEnable() {
        instance = this;
        protocolManager = ProtocolLibrary.getProtocolManager();
        getCommand("mocar").setExecutor(new Commands());
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        cars = new Cars();
    }

    @Override
    public void onDisable() {
        cars.saveConfig();
    }

    public static MoCar getInstance() {
        return instance;
    }

    Cars getCarModule() {
        return cars;
    }
}
