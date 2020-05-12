package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.vehicle.model.Model

/**
 * @author sky_lock
 */
class Engine(private val status: CarStatus, private val model: Model) {
    private val speed: Speed = status.speed
    var currentSpeed = 0f
        private set

    fun consumeFuel(sideInput: Float) {
        if (status.fuel - 0.05f <= 0.0f) {
            status.fuel = 0.0f
            return
        }
        if (speed.approximate() == 0.0f && sideInput == 0.0f) {
            return
        }
        status.fuel = status.fuel - 0.05f
    }

    fun refuel(added: Float): Boolean {
        val current = status.fuel
        val max = model.spec.maxFuel
        if (current >= max) {
            return false
        }
        if (current + added > max) {
            status.fuel = max
            return true
        }
        status.fuel = current + added
        return true
    }

    fun update(passengerInput: Float) {
        if (passengerInput == 0.0f) {
            if (speed.isPositive) {
                speed.frictionalDecelerate()
            }
        } else if (passengerInput < 0.0f) {
            speed.decelerate()
        }
        val maxSpeed = model.spec.maxSpeed
        if (speed.exact() > maxSpeed.value) {
            if (status.fuel <= 0.0f) {
                speed.zeroize()
            }
            currentSpeed = speed.exact()
            return
        }
        if (passengerInput > 0.0f) {
            speed.accelerate()
        }
        if (speed.isNegative) {
            speed.decrease()
        }
        if (status.fuel <= 0.0f) {
            speed.zeroize()
        }
        currentSpeed = speed.exact()
    }

    fun stop() {
        status.speed.zeroize()
    }

    fun speedPerSecond(): Float {
        return speed.exact() * 20
    }

}