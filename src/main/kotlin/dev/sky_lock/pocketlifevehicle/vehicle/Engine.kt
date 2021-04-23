package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.vehicle.model.Model

/**
 * @author sky_lock
 */
class Engine(private val state: State, private val model: Model) {
    private val speed: Speed = state.speed
    var currentSpeed = 0f
        private set

    fun consumeFuel(sideInput: Float) {
        if (state.fuel - 0.05f <= 0.0f) {
            state.fuel = 0.0f
            return
        }
        if (speed.approximate() == 0.0f && sideInput == 0.0f) {
            return
        }
        state.fuel = state.fuel - 0.05f
    }

    fun refuel(added: Float): Boolean {
        val current = state.fuel
        val max = model.spec.maxFuel
        if (current >= max) {
            return false
        }
        if (current + added > max) {
            state.fuel = max
            return true
        }
        state.fuel = current + added
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
            if (state.fuel <= 0.0f) {
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
        if (state.fuel <= 0.0f) {
            speed.zeroize()
        }
        currentSpeed = speed.exact()
    }

    fun stop() {
        state.speed.zeroize()
    }

    fun speedPerSecond(): Float {
        return speed.exact() * 20
    }

}