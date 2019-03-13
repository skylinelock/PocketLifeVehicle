package dev.sky_lock.mocar.car;

import dev.sky_lock.mocar.config.CarsConfig;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author sky_lock
 */

public class CarSet {

    private List<CarModel> carModels;
    private Set<CarEntity> entityCars = new HashSet<>();
    private final CarsConfig config;

    public CarSet() {
        config = new CarsConfig();
        loadModules();
    }

    public void saveConfig() {
        config.save();
    }

    public CarModel getModel(String id) {
        return carModels.stream().filter(model -> model.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    public void addModel(CarModel model) {
        if (carModels.stream().noneMatch(carModel -> carModel.getId().equalsIgnoreCase(model.getId()))) {
            carModels.add(model);
            config.write(carModels);
        }
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

    public boolean spawnAt(CarModel model, Player player, Location location) {
        if (model == null) {
            return false;
        }
        CarEntity car = new CarEntity();
        car.spawn(model, player.getUniqueId(), location);
        entityCars.stream().filter(carEntity -> car.getOwner().equals(carEntity.getOwner())).findFirst().ifPresent(carEntity -> {
            entityCars.remove(carEntity);
        });
        entityCars.add(car);
        return true;
    }

    public void despawn(Player player) {
        entityCars.stream().filter(carEntity -> carEntity.getOwner().equals(player.getUniqueId())).findFirst().ifPresent(carEntity -> {
            carEntity.despawn();
            entityCars.remove(carEntity);
        });
    }

    public CarEntity getCarEntity(Player owner) {
        return entityCars.stream().filter(carEntity -> carEntity.getOwner().equals(owner.getUniqueId())).findFirst().orElse(null);
    }
}
