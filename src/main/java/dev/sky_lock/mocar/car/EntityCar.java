package dev.sky_lock.mocar.car;

import org.bukkit.Location;

/**
 * @author sky_lock
 */

public class EntityCar {
    private final String owner_uuid;
    private final Location location;

    public EntityCar(String owner_uuid, Location location) {
        this.owner_uuid = owner_uuid;
        this.location = location;
    }


}
