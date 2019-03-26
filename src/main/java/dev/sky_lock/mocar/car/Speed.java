package dev.sky_lock.mocar.car;

/**
 * @author sky_lock
 */

public enum Speed {
    SLOWEST("Slowest", 0.20f),
    SLOW("Slow", 0.30f),
    NORMAL("Normal", 0.40f),
    FAST("Fast", 0.50f),
    FASTEST("Fastest", 0.60f);

    private final String label;
    private final float maxSpeed;

    Speed(String label, float maxSpeed) {
        this.label = label;
        this.maxSpeed = maxSpeed;
    }

    public String getLabel() {
        return label;
    }

    float getMax() {
        return maxSpeed;
    }
}
