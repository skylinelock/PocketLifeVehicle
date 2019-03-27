package dev.sky_lock.mocar.car;

import org.bukkit.Location;

import java.util.UUID;

/**
 * @author sky_lock
 */

public class CarEntity {
    private final String owner_uuid;
    private final CarModel model;
    private final Location location;
    private final float fuel;

    CarEntity(String owner_uuid, CarModel model, Location location, float fuel) {
        this.owner_uuid = owner_uuid;
        this.model = model;
        this.location = location;
        this.fuel = fuel;
    }

    public UUID getOwner() {
        return UUID.fromString(owner_uuid);
    }

    public CarModel getModel() {
        return model;
    }

    public Location getLocation() {
        return location;
    }

    public float getFuel() {
        return fuel;
    }
}
