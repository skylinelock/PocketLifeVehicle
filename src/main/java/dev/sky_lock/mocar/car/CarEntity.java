package dev.sky_lock.mocar.car;

import org.bukkit.Location;

import java.util.UUID;

/**
 * @author sky_lock
 */

public class CarEntity {
    private final String ownerUUID;
    private final String modelID;
    private final Location location;
    private final float fuel;

    CarEntity(String ownerUUID, String modelID, Location location, float fuel) {
        this.ownerUUID = ownerUUID;
        this.modelID = modelID;
        this.location = location;
        this.fuel = fuel;
    }

    UUID getOwner() {
        return UUID.fromString(ownerUUID);
    }

    String getModelID() {
        return modelID;
    }

    public Location getLocation() {
        return location;
    }

    public float getFuel() {
        return fuel;
    }
}
