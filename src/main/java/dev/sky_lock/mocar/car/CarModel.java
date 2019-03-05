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
    private final int maxFuel;
    private final int speed;

    public CarModel(String id, String name, List<String> lore, int distancePerLiter, int maxFuel, int speed) {
        this.id = id;
        this.name = name;
        this.lores = lore;
        this.distancePerLiter = distancePerLiter;
        this.maxFuel = maxFuel;
        this.speed = speed;
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

    public int getMaxFuel() {
        return maxFuel;
    }

    public int getSpeed() {
        return speed;
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
        map.put("speed", speed);
        return map;
    }

    public static CarModel deserialize(Map<String, Object> map) {
        String id = (String) map.get("id");
        String name = (String) map.get("name");
        List<String> lores = (List<String>) map.get("lores");
        int distancePerLiter = (int) map.get("distance");
        int maxFuel = (int) map.get("maxfuel");
        int speed = (int) map.get("speed");
        return new CarModel(id, name, lores, distancePerLiter, maxFuel, speed);
    }


}
