package dev.sky_lock.mocar.config;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.CarItem;
import dev.sky_lock.mocar.car.CarModel;
import dev.sky_lock.mocar.util.ListUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

/**
 * @author sky_lock
 */

public class CarsConfig {

    private final Path path;
    private YamlConfiguration config;

    public CarsConfig() {
        ConfigurationSerialization.registerClass(CarModel.class, "CarModel");
        ConfigurationSerialization.registerClass(CarItem.class, "CarItem");
        this.path = MoCar.getInstance().getDataFolder().toPath().resolve("cars.yml");
    }

    public List<CarModel> loadModels() {
        config = ConfigFiles.load(path);
        if (config == null) {
            return new ArrayList<>();
        }
        Object object = config.get("cars");
        if (object == null) {
            return new ArrayList<>();
        }
        List<CarModel> models = ListUtil.checkedListObject(object, CarModel.class);
        if (models == null) {
            return new ArrayList<>();
        }
        models.removeAll(Collections.singleton(null));
        return models;
    }

    public void writeModels(List<CarModel> models) {
        if (config == null) {
            MoCar.getInstance().getLogger().log(Level.WARNING, "Could not write car models to configurations");
            return;
        }
        config.set("cars", models);
        config.get("");
    }

    public void saveToFile() {
        ConfigFiles.save(path, config);
    }

}
