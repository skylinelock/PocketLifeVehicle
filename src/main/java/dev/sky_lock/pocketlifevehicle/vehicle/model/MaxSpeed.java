package dev.sky_lock.pocketlifevehicle.vehicle.model;

/**
 * @author sky_lock
 */

public enum MaxSpeed {
    SLOWEST("最も遅い", 0.20f),
    SLOW("遅い", 0.30f),
    NORMAL("普通", 0.40f),
    FAST("速い", 0.50f),
    FASTEST("最も速い", 0.60f);

    private final String label;
    private final float maxSpeed;

    MaxSpeed(String label, float maxSpeed) {
        this.label = label;
        this.maxSpeed = maxSpeed;
    }

    public String getLabel() {
        return label;
    }

    public float getMax() {
        return maxSpeed;
    }
}