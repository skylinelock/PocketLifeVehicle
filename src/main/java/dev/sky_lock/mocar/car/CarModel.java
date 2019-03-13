package dev.sky_lock.mocar.car;

import dev.sky_lock.mocar.util.ListUtil;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sky_lock
 */

@SerializableAs("CarModel")
public class CarModel implements ConfigurationSerializable {
    private final String id;
    private final String name;
    private final List<String> lores;
    private final float maxFuel;
    private final int maxSpeed;
    private final CarItem item;

    public CarModel(String id, CarItem carItem, String name, List<String> lore, float maxFuel, int maxSpeed) {
        this.id = id;
        this.item = carItem;
        this.name = name;
        this.lores = lore;
        this.maxFuel = maxFuel;
        this.maxSpeed = maxSpeed;
    }

    public CarItem getItem() {
        return item;
    }

    public String getName() {
        return name;
    }

    public List<String> getLores() {
        return lores;
    }

    public float getMaxFuel() {
        return maxFuel;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public String getId() {
        return id;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("item", item);
        map.put("name", name);
        map.put("lores", lores);
        map.put("maxfuel", maxFuel);
        map.put("maxspeed", maxSpeed);
        return map;
    }

    public static CarModel deserialize(Map<String, Object> map) {
        String id = (String) map.get("id");
        CarItem item = (CarItem) map.get("item");
        String name = (String) map.get("name");
        List<String> lores;
        try {
             lores = ListUtil.checkedListObject(map.get("lores"), String.class);
        } catch (ClassCastException ex) {
            lores = Collections.emptyList();
        }
        float maxFuel = (float) ((double) map.get("maxfuel"));
        int speed = (int) map.get("maxspeed");
        return new CarModel(id, item, name, lores, maxFuel, speed);
    }

}
