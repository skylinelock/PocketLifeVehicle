package dev.sky_lock.mocar.car;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sky_lock
 */

@SerializableAs("CollideBox")
public class CollideBox implements ConfigurationSerializable {
    private final float baseSide;
    private final float height;

    public CollideBox(float baseSide, float height) {
        this.baseSide = baseSide;
        this.height = height;
    }

    public static CollideBox deserialize(Map<String, Object> map) {
        float baseSide = (float) (double) map.get("baseside");
        float height = (float) (double) map.get("height");
        return new CollideBox(baseSide, height);
    }

    public float getBaseSide() {
        return baseSide;
    }

    public float getHeight() {
        return height;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("baseside", baseSide);
        map.put("height", height);
        return map;
    }
}
