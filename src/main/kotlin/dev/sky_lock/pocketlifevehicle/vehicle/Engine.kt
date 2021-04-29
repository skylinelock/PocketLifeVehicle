package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.vehicle.model.MaxSpeed

/**
 * @author sky_lock
 */

class Engine(private val tank: FuelTank, private val maxSpeed: MaxSpeed) {
    val speed = Speed()
    private val ZERO = 0.0F
    var currentSpeed = ZERO
        private set

    fun update(sideIn: Float, forIn: Float) {
        if (tank.isEmpty()) {
            speed.zeroize()
            currentSpeed = speed.exact()
            return
        }
        if (speed.exact() > maxSpeed.value) {
            if (forIn < ZERO) {
                speed.decelerate()
            }
            currentSpeed = speed.exact()
            tank.consume()
            return
        }
        if (forIn == ZERO) {
            if (speed.isPositive) {
                speed.frictionalDecelerate()
            }
        } else if (forIn < ZERO) {
            speed.decelerate()
        } else {
            speed.accelerate()
        }
        if (speed.isNegative) {
            speed.decrease()
        }
        currentSpeed = speed.exact()
        if (speed.approximate() != ZERO && sideIn == ZERO) {
            tank.consume()
        }
    }

    fun stop() {
        speed.zeroize()
    }

    fun speedPerSecond(): Float {
        return speed.exact() * 20
    }
}