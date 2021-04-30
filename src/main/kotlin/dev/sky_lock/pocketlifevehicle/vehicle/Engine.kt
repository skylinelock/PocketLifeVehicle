package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.vehicle.model.Model

/**
 * @author sky_lock
 */
private const val ZERO = 0.0F

class Engine(private val tank: FuelTank, private val model: Model) {
    val speed = Speed()
    var currentSpeed = ZERO
        private set

    fun update(sideIn: Float, forIn: Float) {
        if (tank.isEmpty()) {
            speed.zeroize()
            currentSpeed = speed.exact()
            return
        }
        if (speed.exact() > model.spec.maxSpeed.value) {
            if (forIn < ZERO) {
                speed.decelerate()
            } else if (forIn == ZERO) {
                speed.frictionalDecelerate()
            }
            currentSpeed = speed.exact()
            if (!model.flag.eventOnly && model.flag.consumeFuel) {
                tank.consume()
            }
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
        if (speed.approximate() == ZERO && sideIn == ZERO) {
            return
        }
        if (!model.flag.eventOnly && model.flag.consumeFuel) {
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