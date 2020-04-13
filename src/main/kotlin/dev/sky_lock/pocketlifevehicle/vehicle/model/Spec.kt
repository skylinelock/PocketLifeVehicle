package dev.sky_lock.pocketlifevehicle.vehicle.model;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sky_lock
 */

@SerializableAs("Spec")
public class Spec implements ConfigurationSerializable {
    private final float maxFuel;
    private final MaxSpeed maxSpeed;
    private final Capacity capacity;
    private final SteeringLevel steeringLevel;

    public Spec(float maxFuel, MaxSpeed maxSpeed, Capacity capacity, SteeringLevel steeringLevel) {
        this.maxFuel = maxFuel;
        this.maxSpeed = maxSpeed;
        this.capacity = capacity;
        this.steeringLevel = steeringLevel;
    }

    public float getMaxFuel() {
        return maxFuel;
    }

    public MaxSpeed getMaxSpeed() {
        return maxSpeed;
    }

    public Capacity getCapacity() {
        return capacity;
    }

    public SteeringLevel getSteeringLevel() {
        return steeringLevel;
    }

    public static Spec deserialize(Map<String, Object> map) {
        float maxFuel = (float) ((double) map.get("maxfuel"));
        MaxSpeed speed = MaxSpeed.values()[(int) map.get("maxspeed")];
        Capacity capacity = Capacity.from((int) map.get("capacity"));
        SteeringLevel steeringLevel = SteeringLevel.valueOf(String.valueOf(map.get("steering")));
        return new Spec(maxFuel, speed, capacity, steeringLevel);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("maxfuel", maxFuel);
        map.put("maxspeed", maxSpeed.ordinal());
        map.put("capacity", capacity.value());
        map.put("steering", steeringLevel.toString());

        return map;
    }
}