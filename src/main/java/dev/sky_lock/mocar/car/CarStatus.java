package dev.sky_lock.mocar.car;

import org.bukkit.Location;

/**
 * @author sky_lock
 */

public class CarStatus {
    private final Speed speed;
    private float fuel;
    private float yaw;
    private boolean locked = true;
    private boolean wieldHand = true;
    private Location location;

    CarStatus() {
        this.speed = new Speed();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setFuel(float fuel) {
        this.fuel = fuel;
    }

    Speed getSpeed() {
        return speed;
    }

    public float getFuel() {
        return fuel;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isWieldHand() {
        return wieldHand;
    }

    public void setWieldHand(boolean wieldHand) {
        this.wieldHand = wieldHand;
    }

    public boolean isLocked() {
        return this.locked;
    }

    float getYaw() {
        return yaw;
    }

    void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
