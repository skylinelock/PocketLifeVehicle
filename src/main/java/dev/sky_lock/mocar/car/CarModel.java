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
    private final CollideBox collideBox;
    private final SteeringLevel steeringLevel;
    private final float height;
    private final CarSound sound;

    CarModel(String id, CarItem carItem, String name, List<String> lore, float maxFuel, MaxSpeed maxSpeed, Capacity capacity, SteeringLevel steeringLevel, CollideBox collideBox, float height, CarSound sound) {
        this.id = id;
        this.item = carItem;
        this.name = name;
        this.lores = lore;
        this.maxFuel = maxFuel;
        this.maxSpeed = maxSpeed;
        this.capacity = capacity;
        this.collideBox = collideBox;
        this.steeringLevel = steeringLevel;
        this.height = height;
        this.sound = sound;
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
                lores = ListUtil.checkListTypeDynamically(mapObj, String.class);
            } catch (ClassCastException ex) {
                lores = Collections.emptyList();
            }
        }
        float maxFuel = (float) ((double) map.get("maxfuel"));
        MaxSpeed speed = MaxSpeed.values()[(int) map.get("maxspeed")];
        Capacity capacity = Capacity.from((int) map.get("capacity"));
        CollideBox collideBox = (CollideBox) map.get("collidebox");
        SteeringLevel steeringLevel = SteeringLevel.valueOf(String.valueOf(map.get("steering")));
        float height = (float) ((double) map.get("height"));
        CarSound sound = CarSound.valueOf(String.valueOf(map.get("sound")));
        return CarModelBuilder.of(id)
                .name(name)
                .item(item)
                .lores(lores)
                .maxFuel(maxFuel)
                .maxSpeed(speed)
                .capacity(capacity)
                .collideBox(collideBox.getBaseSide(), collideBox.getHeight())
                .steering(steeringLevel)
                .height(height)
                .sound(sound)
                .build();
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

    public ItemStack getItemStack() {
        return ItemStackBuilder.of(item.getType(), 1).name(name).durability(item.getDamage()).unbreakable(true).build();
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

    public CollideBox getCollideBox() {
        return collideBox;
    }

    public float getHeight() {
        return height;
    }

    public CarSound getSound() {
        return sound;
    }

    public SteeringLevel getSteeringLevel() {
        return steeringLevel;
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
        map.put("collidebox", collideBox);
        map.put("height", height);
        map.put("steering", steeringLevel.toString());
        map.put("sound", sound.toString());
        return map;
    }

}
