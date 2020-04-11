package dev.sky_lock.pocketlifevehicle.vehicle;

import org.bukkit.Location;

import java.util.UUID;

/**
 * @author sky_lock
 */

public class CarEntity {
    private final String ownerUuid;
    private final String modelId;
    private final Location location;
    private final float fuel;

    CarEntity(String ownerUuid, String modelId, Location location, float fuel) {
        this.ownerUuid = ownerUuid;
        this.modelId = modelId;
        this.location = location;
        this.fuel = fuel;
    }

    UUID getOwner() {
        return UUID.fromString(ownerUuid);
    }

    String getModelId() {
        return modelId;
    }

    public Location getLocation() {
        return location;
    }

    public float getFuel() {
        return fuel;
    }
}
