package dev.sky_lock.mocar.car;

/**
 * @author sky_lock
 */

public enum Speed {
    SLOWEST(0.20f),
    SLOW(0.30f),
    NORMAL(0.40f),
    FAST(0.50f),
    FASTEST(0.60f);

    private final float maxSpeed;

    private Speed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    float getMax() {
        return maxSpeed;
    }
}
