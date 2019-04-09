package dev.sky_lock.mocar.car;

import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * @author sky_lock
 */

class SeatPositionControl {
    private final static float RADIUS = 2.0F;

    Location calculate(Location location, SeatPosition position) {
        Vector vector = location.getDirection();
        switch (position) {
            case ONE_DRIVER:
                return location;
            case TWO_DRIVER:
                vector.multiply(1);
                return location.clone().add(vector);
            case TWO_PASSENGER:
                vector.multiply(1).rotateAroundY(Math.toRadians(180));
                return location.clone().add(vector);
            case FOUR_DRIVER:
                vector.multiply(0.5).rotateAroundY(Math.toRadians(-90));
                return location.clone().add(vector);
            case FOUR_PASSENGER:
                vector.multiply(0.5).rotateAroundY(Math.toRadians(90));
                return location.clone().add(vector);
            case FOUR_REAR_LEFT:
                vector.multiply(1.3).rotateAroundY(Math.toRadians(158));
                return location.clone().add(vector);
            case FOUR_REAR_RIGHT:
                vector.multiply(1.3).rotateAroundY(Math.toRadians(-158));
                return location.clone().add(vector);
        }
        return null;
    }
}