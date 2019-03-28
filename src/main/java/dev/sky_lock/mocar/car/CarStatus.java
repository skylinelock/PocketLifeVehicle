package dev.sky_lock.mocar.car;

/**
 * @author sky_lock
 */

public class CarStatus {
    private final Speed speed;
    private float fuel;
    private float yaw;
    private boolean locked = true;

    CarStatus() {
        this.speed = new Speed();
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
