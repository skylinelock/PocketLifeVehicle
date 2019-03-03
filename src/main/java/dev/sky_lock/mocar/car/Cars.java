package dev.sky_lock.mocar.car;

import dev.sky_lock.mocar.config.CarsConfiguration;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author sky_lock
 */

public class Cars {

    private List<CarModel> carModels;
    private Set<CarEntity> carEntities;
    private final CarsConfiguration config;

    public Cars() {
        config = new CarsConfiguration();
        loadModules();
    }

    public void saveConfig() {
        config.save();
    }

    public void addModel(CarModel model) {
        carModels.add(model);
        config.write(carModels);
    }

    public void removeModel(CarModel model) {
        carModels.remove(model);
        config.write(carModels);
    }

    public void removeModel(String name) {
        carModels = carModels.stream().filter(model -> model != null && !model.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
    }

    public List<CarModel> getCarModels() {
        return carModels;
    }

    public void reloadConfig() {
        config.save();
        loadModules();
    }

    public void loadModules() {
        carModels = config.load();
    }
}
