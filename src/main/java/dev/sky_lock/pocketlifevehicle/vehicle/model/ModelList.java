package dev.sky_lock.pocketlifevehicle.vehicle.model;

import dev.sky_lock.pocketlifevehicle.config.CarsConfig;
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
    private static final List<Model> models = config.loadModels();

    public static void saveConfig() {
        config.saveToFile();
    }

    public static void reloadConfig() {
        models.clear();
        models.addAll(config.loadModels());
    }

    public static Optional<Model> of(String id) {
        return models.stream().filter(model -> model.getId().equalsIgnoreCase(id)).findFirst();
    }

    public static boolean add(Model model) {
        if (models.stream().noneMatch(carModel -> carModel.getId().equalsIgnoreCase(model.getId()))) {
            models.add(model);
            config.writeModels(models);
            return true;
        }
        return false;
    }

    public static boolean remove(String id) {
        return of(id).map(model -> {
            models.remove(model);
            config.writeModels(models);
            return true;
        }).orElse(false);
    }

    public static List<Model> unmodified() {
        return Collections.unmodifiableList(models);
    }

    public static boolean exists(String id) {
        return models.stream().anyMatch(carModel -> carModel.getId().equalsIgnoreCase(id));
    }

    public static Model of(ItemStack itemStack) {
        if (itemStack.getType() == Material.AIR) {
            return null;
        }
        return models.stream().filter(model -> {
            ItemMeta meta = Objects.requireNonNull(itemStack.getItemMeta());
            if (!meta.hasDisplayName()) {
                return false;
            }
            String displayName = meta.getDisplayName();
            int modelId = meta.getCustomModelData();

            String carName = model.getName();

            ModelItem modelItem = model.getCarItem();
            int carModelId = modelItem.getModelId();
            Material type = modelItem.getType();

            return itemStack.getType() == type && displayName.equals(carName) && modelId == carModelId;
        }).findFirst().orElse(null);
    }
}
