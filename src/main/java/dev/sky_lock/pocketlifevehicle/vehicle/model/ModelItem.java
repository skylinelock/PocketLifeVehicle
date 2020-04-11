package dev.sky_lock.pocketlifevehicle.vehicle.model;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sky_lock
 */

@SerializableAs("ModelItem")
public class ModelItem implements ConfigurationSerializable {

    private final Material type;
    private final int modelId;

    public ModelItem(Material type, int modelId) {
        this.type = type;
        this.modelId = modelId;
    }

    public static ModelItem deserialize(Map<String, Object> map) {
        Material type = Material.valueOf((String) map.get("type"));
        int modelId = Integer.valueOf(String.valueOf(map.get("modelid")));
        return new ModelItem(type, modelId);
    }

    public Material getType() {
        return type;
    }

    public int getModelId() {
        return modelId;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type.name());
        map.put("modelid", modelId);
        return map;
    }
}
