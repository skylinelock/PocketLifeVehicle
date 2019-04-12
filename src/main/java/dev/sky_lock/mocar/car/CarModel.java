package dev.sky_lock.mocar.car;

import dev.sky_lock.mocar.item.ItemStackBuilder;
import dev.sky_lock.mocar.util.ListUtil;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;

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
    private final MaxSpeed maxSpeed;
    private final CarItem item;
    private final Capacity capacity;

    public CarModel(String id, CarItem carItem, String name, List<String> lore, float maxFuel, MaxSpeed maxSpeed, Capacity capacity) {
        this.id = id;
        this.item = carItem;
        this.name = name;
        this.lores = lore;
        this.maxFuel = maxFuel;
        this.maxSpeed = maxSpeed;
        this.capacity = capacity;
    }

    public ItemStack getItemStack() {
        return ItemStackBuilder.of(item.getType(), 1).name(name).durability(item.getDamage()).unbreakable(true).build();
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

    public static CarModel deserialize(Map<String, Object> map) {
        String id = (String) map.get("id");
        CarItem item = (CarItem) map.get("item");
        String name = (String) map.get("name");
        List<String> lores;
        Object mapObj = map.get("lores");
        if (mapObj == null) {
            lores = Collections.emptyList();
        } else {
            try {
                lores = ListUtil.checkedListObject(mapObj, String.class);
            } catch (ClassCastException ex) {
                lores = Collections.emptyList();
            }
        }
        float maxFuel = (float) ((double) map.get("maxfuel"));
        MaxSpeed speed = MaxSpeed.values()[(int) map.get("maxspeed")];
        Capacity capacity = Capacity.from((int) map.get("capacity"));
        return new CarModel(id, item, name, lores, maxFuel, speed, capacity);
    }

    public String getId() {
        return id;
    }

    public MaxSpeed getMaxSpeed() {
        return maxSpeed;
    }

    public Capacity getCapacity() {
        return capacity;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("item", item);
        map.put("name", name);
        map.put("lores", lores);
        map.put("maxfuel", maxFuel);
        map.put("maxspeed", maxSpeed.ordinal());
        map.put("capacity", capacity.value());
        return map;
    }

}
