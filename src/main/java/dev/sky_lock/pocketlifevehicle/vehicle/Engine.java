package dev.sky_lock.pocketlifevehicle.vehicle;

import dev.sky_lock.pocketlifevehicle.vehicle.model.MaxSpeed;
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model;

/**
 * @author sky_lock
 */

class Engine {
    private final CarStatus status;
    private final Model model;
    private final Speed speed;
    private float currentSpeed;

    Engine(CarStatus status, Model model) {
        this.status = status;
        this.model = model;
        this.speed = status.getSpeed();
    }

    void consumeFuel(float sideInput) {
        if (speed.approximate() == 0.0F) {
            if (sideInput == 0.0F) {
                return;
            }
            if (status.getFuel() - 0.05F <= 0.0F) {
                status.setFuel(0.0F);
                return;
            }
        }
        status.setFuel(status.getFuel() - 0.05F);
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


    void update(float passengerInput) {
        if (passengerInput == 0.0f) {
            if (speed.isPositive()) {
                speed.frictionalDecelerate();
            }
        } else if (passengerInput < 0.0f) {
            speed.decelerate();
        }

        MaxSpeed maxSpeed;
        if (model.getMaxSpeed() == null) {
            maxSpeed = MaxSpeed.NORMAL;
        } else {
            maxSpeed = model.getMaxSpeed();
        }

        if (speed.exact() > maxSpeed.getMax()) {
            if (status.getFuel() <= 0.0f) {
                speed.zeroize();
            }
            this.currentSpeed = speed.exact();
            return;
        }
        if (passengerInput > 0.0f) {
            speed.accelerate();
        }
        if (speed.isNegative()) {
            speed.decrease();
        }
        if (status.getFuel() <= 0.0f) {
            speed.zeroize();
        }
        this.currentSpeed = speed.exact();
    }

    float getCurrentSpeed() {
        return currentSpeed;
    }

    void stop() {
        status.getSpeed().zeroize();
    }

    float speedPerSecond() {
        return speed.exact() * 20;
    }

}
