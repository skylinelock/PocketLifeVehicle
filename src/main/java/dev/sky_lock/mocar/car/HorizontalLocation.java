package dev.sky_lock.mocar.car;

/**
 * @author sky_lock
 */

class HorizontalLocation {
    private final double x;
    private final double z;

    HorizontalLocation(double x, double z) {
        this.x = x;
        this.z = z;
    }

    double getX() {
        return x;
    }

    double getZ() {
        return z;
    }
}
