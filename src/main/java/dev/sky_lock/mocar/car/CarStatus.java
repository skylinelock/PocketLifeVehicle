package dev.sky_lock.mocar.car;

import java.math.BigDecimal;

/**
 * @author sky_lock
 */

public class CarStatus {
    private float fuel;
    private BigDecimal speed;

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

    public void useFuel(float used) {
        if (speed.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }
        if (fuel < 0.0f) {
            return;
        }
        this.fuel -= used;
    }
}
