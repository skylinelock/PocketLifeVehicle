package dev.sky_lock.mocar.car;

import dev.sky_lock.mocar.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sky_lock
 */

@SerializableAs("CarItem")
public class CarItem implements ConfigurationSerializable {

    private final Material type;
    private final short damage;

    public CarItem(Material type, short damage) {
        this.type = type;
        this.damage = damage;
    }

    public ItemStack getStack() {
        return new ItemStackBuilder(type, 1).damage(damage).build();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type.name());
        map.put("damage", damage);
        return map;
    }

    public static CarItem deserialize(Map<String, Object> map) {
        Material type = Material.valueOf((String) map.get("type"));
        short damage = (short)((double) map.get("damage"));
        return new CarItem(type, damage);
    }
}
