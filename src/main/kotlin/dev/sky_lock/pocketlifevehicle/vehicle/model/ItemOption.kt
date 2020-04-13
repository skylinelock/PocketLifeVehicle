package dev.sky_lock.pocketlifevehicle.vehicle.model;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sky_lock
 */

@SerializableAs("ItemOption")
public class ItemOption implements ConfigurationSerializable {

    private final Material type;
    private final int id;
    private ItemPosition position = ItemPosition.HEAD;

    public ItemOption(Material type, int id) {
        this.type = type;
        this.id = id;
    }

    public ItemOption(Material type, int id, ItemPosition position) {
        this(type, id);
        setPosition(position);
    }

    public Material getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public void setPosition(ItemPosition position) {
        this.position = position;
    }

    public ItemPosition getPosition() {
        return position;
    }

    public static ItemOption deserialize(Map<String, Object> map) {
        Material type = Material.valueOf((String) map.get("type"));
        int id = Integer.parseInt(String.valueOf(map.get("id")));
        ItemPosition position = ItemPosition.valueOf(String.valueOf(map.get("position")));
        return new ItemOption(type, id, position);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type.name());
        map.put("id", id);
        map.put("position", position.toString());
        return map;
    }
}