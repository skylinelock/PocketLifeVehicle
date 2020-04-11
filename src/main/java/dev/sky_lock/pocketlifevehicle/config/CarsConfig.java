package dev.sky_lock.pocketlifevehicle.config;

import dev.sky_lock.pocketlifevehicle.PLVehicle;
import dev.sky_lock.pocketlifevehicle.vehicle.model.ModelItem;
import dev.sky_lock.pocketlifevehicle.vehicle.model.CollideBox;
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model;
import dev.sky_lock.pocketlifevehicle.util.TypeChecks;
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
        ConfigurationSerialization.registerClass(Model.class);
        ConfigurationSerialization.registerClass(ModelItem.class);
        ConfigurationSerialization.registerClass(CollideBox.class);
        this.path = PLVehicle.getInstance().getDataFolder().toPath().resolve("vehicles.yml");
    }

    public List<Model> loadModels() {
        config = ConfigFiles.load(path);
        if (config == null) {
            return new ArrayList<>();
        }
        Object object = config.get("cars");
        if (object == null) {
            return new ArrayList<>();
        }
        List<Model> models = TypeChecks.checkListTypeDynamically(object, Model.class);
        if (models == null) {
            return new ArrayList<>();
        }
        models.removeAll(Collections.singleton(null));
        return models;
    }

    public void writeModels(List<Model> models) {
        if (config == null) {
            PLVehicle.getInstance().getLogger().log(Level.WARNING, "Could not write vehicle models to configurations");
            return;
        }
        config.set("cars", models);
        config.get("");
    }

    public void saveToFile() {
        ConfigFiles.save(path, config);
    }

}
