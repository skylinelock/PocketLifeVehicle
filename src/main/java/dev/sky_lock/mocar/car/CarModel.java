package dev.sky_lock.mocar.car;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

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
    private final int distancePerLiter;
    private final float maxFuel;
    private final int maxSpeed;

    public CarModel(String id, String name, List<String> lore, int distancePerLiter, float maxFuel, int maxSpeed) {
        this.id = id;
        this.name = name;
        this.lores = lore;
        this.distancePerLiter = distancePerLiter;
        this.maxFuel = maxFuel;
        this.maxSpeed = maxSpeed;
    }

    public String getName() {
        return name;
    }

    public List<String> getLores() {
        return lores;
    }

    public int getDistancePerLiter() {
        return distancePerLiter;
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
        map.put("name", name);
        map.put("lores", lores);
        map.put("distance", distancePerLiter);
        map.put("maxfuel", maxFuel);
        map.put("maxspeed", maxSpeed);
        return map;
    }

    public static CarModel deserialize(Map<String, Object> map) {
        String id = (String) map.get("id");
        String name = (String) map.get("name");
        List<String> lores = (List<String>) map.get("lores");
        int distancePerLiter = (int) map.get("distance");
        float maxFuel = (float) ((double) map.get("maxfuel"));
        int speed = (int) map.get("maxspeed");
        return new CarModel(id, name, lores, distancePerLiter, maxFuel, speed);
    }


}
