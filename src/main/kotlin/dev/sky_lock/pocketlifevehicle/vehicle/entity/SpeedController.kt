package dev.sky_lock.pocketlifevehicle.vehicle.entity

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * @author sky_lock
 */
class SpeedController {
    private var exactSpeed = BigDecimal.ZERO

    val isPositive: Boolean
        get() = exactSpeed > BigDecimal.ZERO

    val isNegative: Boolean
        get() = exactSpeed < BigDecimal.ZERO

    fun accelerate() {
        exactSpeed += acceleration
    }

    fun frictionalDecelerate() {
        exactSpeed -= acceleration
    }

    fun sideBreakDecelerate() {
        exactSpeed -= sideBreakDeceleration
    }

    fun sideBreakAccelerate() {
        exactSpeed += sideBreakDeceleration
    }

    fun decelerate() {
        exactSpeed -= acceleration + frictionalDeceleration
    }

    fun decrease() {
        exactSpeed *= magnification
    }

    fun approximate(): Float {
        return exactSpeed.setScale(3, RoundingMode.HALF_UP).toFloat()
    }

    val isApproximateZero: Boolean
        get() = approximate() == 0.0f

    fun exact(): Float {
        return exactSpeed.toFloat()
    }

    fun zero() {
        exactSpeed = BigDecimal.ZERO
    }

    companion object {
        private val acceleration = BigDecimal("0.0085")
        private val frictionalDeceleration = BigDecimal("0.010")
        private val sideBreakDeceleration = BigDecimal("0.0075")
        private val magnification = BigDecimal("0.85")
    }
}