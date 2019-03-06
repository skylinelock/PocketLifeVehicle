package dev.sky_lock.mocar.config;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.CarModel;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

/**
 * @author sky_lock
 */

public class CarsConfig {

    private YamlConfiguration carConfig;

    public CarsConfig() {
        ConfigurationSerialization.registerClass(CarModel.class, "CarModel");
    }

    public List<CarModel> load() {
        carConfig = ConfigFiles.load("cars.yml");
        if (carConfig == null) {
            return Collections.emptyList();
        }
        List<CarModel> models = (ArrayList<CarModel>) carConfig.get("cars");
        if (models == null) {
            return new ArrayList<CarModel>();
        }
        return models;
    }

    public void write(List<CarModel> models) {
        if (carConfig == null) {
            MoCar.getInstance().getLogger().log(Level.WARNING, "Could not write to car configution");
            return;
        }
        carConfig.set("cars", models);
    }

    public void save() {
        ConfigFiles.save(MoCar.getInstance().getDataFolder().toPath().resolve("cars.yml"), carConfig);
    }

}
