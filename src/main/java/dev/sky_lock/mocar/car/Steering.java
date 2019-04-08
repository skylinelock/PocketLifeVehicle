package dev.sky_lock.mocar.car;

/**
 * @author sky_lock
 */

class Steering {
    private final CarStatus status;

    Steering(CarStatus status) {
        this.status = status;
    }

    void right() {
        if (Math.round(status.getFuel()) == 0 || status.getSpeed().isApproximateZero()) {
            return;
        }
        if (status.getSpeed().isPositive()) {
            status.setYaw(status.getYaw() + 4.0F);
        } else {
            status.setYaw(status.getYaw() - 4.0F);
        }
    }

    void left() {
        if (Math.round(status.getFuel()) == 0 ||status.getSpeed().isApproximateZero()) {
            return;
        }
        if (status.getSpeed().isPositive()) {
            status.setYaw(status.getYaw() - 4.0F);
        } else {
            status.setYaw(status.getYaw() + 4.0F);
        }
    }
}
