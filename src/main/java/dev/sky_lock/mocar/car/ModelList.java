package dev.sky_lock.mocar.car;

import dev.sky_lock.mocar.config.CarsConfig;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author sky_lock
 */

public class ModelList {

    private static final CarsConfig config = new CarsConfig();
    private static final List<CarModel> carModels = config.loadModels();

    public static void saveConfig() {
        config.saveToFile();
    }

    public static void reloadConfig() {
        carModels.clear();
        carModels.addAll(config.loadModels());
    }

    public static Optional<CarModel> of(String id) {
        return carModels.stream().filter(model -> model.getId().equalsIgnoreCase(id)).findFirst();
    }

    public static boolean add(CarModel model) {
        if (carModels.stream().noneMatch(carModel -> carModel.getId().equalsIgnoreCase(model.getId()))) {
            carModels.add(model);
            config.writeModels(carModels);
            return true;
        }
        return false;
    }

    public static boolean remove(String id) {
        return of(id).map(model -> {
            carModels.remove(model);
            config.writeModels(carModels);
            return true;
        }).orElse(false);
    }

    public static List<CarModel> unmodified() {
        return Collections.unmodifiableList(carModels);
    }

    public static boolean exists(String id) {
        return carModels.stream().anyMatch(carModel -> carModel.getId().equalsIgnoreCase(id));
    }

    public static CarModel of(ItemStack itemStack) {
        if (itemStack.getType() == Material.AIR) {
            return null;
        }
        return carModels.stream().filter(model -> {
            ItemMeta meta = Objects.requireNonNull(itemStack.getItemMeta());
            if (!meta.hasDisplayName()) {
                return false;
            }
            int modelId = meta.getCustomModelData();
            String displayName = meta.getDisplayName();

            String carName = model.getName();
            CarItem carItem = model.getCarItem();
            int carModelId = carItem.getModelId();
            Material type = carItem.getType();

            return itemStack.getType() == type && displayName.equals(carName) && modelId == carModelId;
        }).findFirst().orElse(null);
    }
}
