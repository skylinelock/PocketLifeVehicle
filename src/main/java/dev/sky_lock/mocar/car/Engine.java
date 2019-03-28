package dev.sky_lock.mocar.car;

/**
 * @author sky_lock
 */

class Engine {
    private final CarStatus status;
    private final CarModel model;
    private final Speed speed;

    Engine(CarStatus status, CarModel model) {
        this.status = status;
        this.model = model;
        this.speed = status.getSpeed();
    }

    void consumeFuel(float sideInput) {
        if (speed.approximate() == 0.0F) {
            if (sideInput == 0.0F) {
                return;
            }
            status.setFuel(status.getFuel() - 0.05F);
        } else {
            status.setFuel(status.getFuel() - 0.05F);
        }
    }

    boolean refuel(float added) {
        float current = status.getFuel();
        float max = model.getMaxFuel();
        if (current >= max) {
            return false;
        }
        if (current + added > max) {
            status.setFuel(max);
            return true;
        }
        status.setFuel(current + added);
        return true;
    }


    float speedPerTick(float passengerInput) {
        if (passengerInput == 0.0f) {
            if (speed.isPositive()) {
                speed.frictionalDecelerate();
            }
        } else if (passengerInput < 0.0f) {
            speed.decelerate();
        }

        MaxSpeed maxSpeed;
        if (model.getMaxSpeed() > MaxSpeed.values().length) {
            maxSpeed = MaxSpeed.NORMAL;
        } else {
            maxSpeed = MaxSpeed.values()[model.getMaxSpeed() - 1];
        }

        if (speed.exact() > maxSpeed.getMax()) {
            if (status.getFuel() <= 0.0f) {
                speed.zero();
            }
            return speed.exact();
        }
        if (passengerInput > 0.0f) {
            speed.accelerate();
        }
        if (speed.isNegative()) {
            speed.decrease();
        }
        if (status.getFuel() <= 0.0f) {
            speed.zero();
        }
        return speed.exact();
    }

    float speedPerSecond() {
        return speed.exact() * 20;
    }

}
