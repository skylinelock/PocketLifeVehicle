package dev.sky_lock.mocar.car;

import dev.sky_lock.mocar.config.CarsConfiguration;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author sky_lock
 */

public class Cars {

    private List<CarModel> carModels;
    private Set<Car> carEntities = new HashSet<>();
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

    public void removeModel(String id) {
        carModels = carModels.stream().filter(model -> model != null && !model.getId().equalsIgnoreCase(id)).collect(Collectors.toList());
        config.write(carModels);
    }

    public List<CarModel> getCarModels() {
        return carModels;
    }

    public void reloadConfig() {
        loadModules();
    }

    public void loadModules() {
        carModels = config.load();
    }

    public void spawnAt(Player player, Location location) {
        Car car = new Car();
        car.spawn(player.getUniqueId(), location);
        carEntities.stream().filter(carEntity -> car.getOwner().equals(carEntity.getOwner())).findFirst().ifPresent(carEntity -> {
            carEntities.remove(carEntity);
        });
        carEntities.add(car);
    }

    public Car getCar(Player owner) {
        return carEntities.stream().filter(car -> car.getOwner().equals(owner.getUniqueId())).findFirst().orElse(null);
    }
}
