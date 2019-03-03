package dev.sky_lock.mocar;

import dev.sky_lock.mocar.car.Cars;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author sky_lock
 */

public class MoCar extends JavaPlugin {

    private static MoCar instance;
    private Cars cars;

    @Override
    public void onEnable() {
        instance = this;
        getCommand("mocar").setExecutor(new Commands());
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
