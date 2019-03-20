package dev.sky_lock.mocar.car;

import java.math.BigDecimal;

/**
 * @author sky_lock
 */

public class CarStatus {
    private float fuel;
    private BigDecimal speed;
    private boolean locked;

    public void setFuel(float fuel) {
        this.fuel = fuel;
    }

    public void setSpeed(BigDecimal speed) {
        this.speed = speed;
    }

    public BigDecimal getSpeed() {
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

    public void useFuel(float used) {
        BigDecimal roundedSpeed = speed.setScale(4, BigDecimal.ROUND_HALF_UP);
        if (roundedSpeed.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }
        if (fuel < 0.0f) {
            return;
        }
        this.fuel -= used;
    }
}
