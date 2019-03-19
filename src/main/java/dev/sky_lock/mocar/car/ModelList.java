package dev.sky_lock.mocar.car;

import dev.sky_lock.mocar.config.CarsConfig;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    public static CarModel get(String id) {
        return carModels.stream().filter(model -> model.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    public static boolean add(CarModel model) {
        if (carModels.stream().noneMatch(carModel -> carModel.getId().equalsIgnoreCase(model.getId()))) {
            carModels.add(model);
            config.writeModels(carModels);
            return true;
        }
        return false;
    }

    public static void remove(String id) {
        carModels.retainAll(carModels.stream().filter(model -> model != null && !model.getId().equalsIgnoreCase(id)).collect(Collectors.toList()));
        config.writeModels(carModels);
    }

    public static List<CarModel> unmodified() {
        return Collections.unmodifiableList(carModels);
    }

    public static boolean exists(String id) {
        return carModels.stream().anyMatch(carModel -> carModel.getId().equalsIgnoreCase(id));
    }

    public static CarModel getModel(ItemStack itemStack) {
        return carModels.stream().filter(model -> model.getItem().getStack(model.getName()).isSimilar(itemStack)).findFirst().orElse(null);
    }
}
