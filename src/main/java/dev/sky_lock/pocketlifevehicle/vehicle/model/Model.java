package dev.sky_lock.pocketlifevehicle.vehicle.model;

import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder;
import dev.sky_lock.pocketlifevehicle.util.TypeChecks;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sky_lock
 */

@SerializableAs("Model")
public class Model implements ConfigurationSerializable {
    private final String id;
    private final String name;
    private final List<String> lore;
    private final float maxFuel;
    private final MaxSpeed maxSpeed;
    private final ModelItem item;
    private final Capacity capacity;
    private final CollideBox collideBox;
    private final SteeringLevel steeringLevel;
    private final float height;
    private final Sound sound;
    private final ModelPosition position;

    Model(String id,
          ModelItem modelItem,
          String name,
          List<String> lore,
          float maxFuel,
          MaxSpeed maxSpeed,
          Capacity capacity,
          SteeringLevel steeringLevel,
          CollideBox collideBox,
          float height,
          Sound sound,
          ModelPosition position) {
        this.id = id;
        this.item = modelItem;
        this.name = name;
        this.lore = lore;
        this.maxFuel = maxFuel;
        this.maxSpeed = maxSpeed;
        this.capacity = capacity;
        this.collideBox = collideBox;
        this.steeringLevel = steeringLevel;
        this.height = height;
        this.sound = sound;
        this.position = position;
    }

    public static Model deserialize(Map<String, Object> map) {
        String id = (String) map.get("id");
        ModelItem item = (ModelItem) map.get("item");
        String name = (String) map.get("name");
        List<String> lore;
        Object mapObj = map.get("lore");
        if (mapObj == null) {
            lore = Collections.emptyList();
        } else {
            try {
                lore = TypeChecks.checkListTypeDynamically(mapObj, String.class);
            } catch (ClassCastException ex) {
                lore = Collections.emptyList();
            }
        }
        float maxFuel = (float) ((double) map.get("maxfuel"));
        MaxSpeed speed = MaxSpeed.values()[(int) map.get("maxspeed")];
        Capacity capacity = Capacity.from((int) map.get("capacity"));
        CollideBox collideBox = (CollideBox) map.get("collidebox");
        SteeringLevel steeringLevel = SteeringLevel.valueOf(String.valueOf(map.get("steering")));
        float height = (float) ((double) map.get("height"));
        Sound sound = Sound.valueOf(String.valueOf(map.get("sound")));
        ModelPosition position = ModelPosition.valueOf(String.valueOf(map.get("position")));
        return ModelBuilder.of(id)
                .name(name)
                .item(item)
                .lore(lore)
                .maxFuel(maxFuel)
                .maxSpeed(speed)
                .capacity(capacity)
                .collideBox(collideBox.getBaseSide(), collideBox.getHeight())
                .steering(steeringLevel)
                .height(height)
                .sound(sound)
                .modelPosition(position)
                .build();
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }

    public float getMaxFuel() {
        return maxFuel;
    }

    public ModelItem getCarItem() {
        return item;
    }

    public ItemStack getItemStack() {
        return ItemStackBuilder.of(item.getType(), 1).name(name).customModelData(item.getModelId()).unbreakable(true).itemFlags(ItemFlag.values()).build();
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

    public Sound getSound() {
        return sound;
    }

    public SteeringLevel getSteeringLevel() {
        return steeringLevel;
    }

    public ModelPosition getModelPosition() {
        return position;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("item", item);
        map.put("name", name);
        map.put("lore", lore);
        map.put("maxfuel", maxFuel);
        map.put("maxspeed", maxSpeed.ordinal());
        map.put("capacity", capacity.value());
        map.put("collidebox", collideBox);
        map.put("height", height);
        map.put("steering", steeringLevel.toString());
        map.put("sound", sound.toString());
        map.put("position", position.toString());
        return map;
    }

}
