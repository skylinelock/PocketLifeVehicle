package dev.sky_lock.mocar.car;

import dev.sky_lock.mocar.config.CarsConfig;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;
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

    public static Optional<CarModel> get(String id) {
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
        return get(id).map(model -> {
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

    public static CarModel get(ItemStack itemStack) {
        return carModels.stream().filter(model -> {
            ItemMeta meta = itemStack.getItemMeta();
            if (!meta.hasDisplayName()) {
                return false;
            }
            int damage = ((Damageable) itemStack.getItemMeta()).getDamage();
            ItemStack modelItem = model.getItem().getStack(model.getName());
            ItemMeta modelMeta = modelItem.getItemMeta();
            int modelDamage = ((Damageable) modelItem.getItemMeta()).getDamage();
            String displayName = itemStack.getItemMeta().getDisplayName();
            String carName = modelMeta.getDisplayName();
            return itemStack.getType() == modelItem.getType() && displayName.equals(carName) && damage == modelDamage;
        }).findFirst().orElse(null);
    }
}
