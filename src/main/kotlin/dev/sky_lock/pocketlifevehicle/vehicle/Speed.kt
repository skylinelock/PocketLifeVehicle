package dev.sky_lock.pocketlifevehicle.vehicle

import java.math.BigDecimal

/**
 * @author sky_lock
 */
internal class Speed {
    private var exactSpeed = BigDecimal.ZERO

    val isPositive: Boolean
        get() = exactSpeed > BigDecimal.ZERO

    val isNegative: Boolean
        get() = exactSpeed < BigDecimal.ZERO

    fun accelerate() {
        exactSpeed = exactSpeed.add(acceleration)
    }

    fun frictionalDecelerate() {
        exactSpeed = exactSpeed.subtract(acceleration)
    }

    fun decelerate() {
        exactSpeed = exactSpeed.subtract(acceleration.add(frictionalDeceleration))
    }

    fun decrease() {
        exactSpeed = exactSpeed.multiply(magnification)
    }

    fun approximate(): Float {
        return exactSpeed.setScale(3, BigDecimal.ROUND_HALF_UP).toFloat()
    }

    val isApproximateZero: Boolean
        get() = approximate() == 0.0f

    fun exact(): Float {
        return exactSpeed.toFloat()
    }

    fun zeroize() {
        exactSpeed = BigDecimal.ZERO
    }

    companion object {
        private val acceleration = BigDecimal("0.0085")
        private val frictionalDeceleration = BigDecimal("0.010")
        private val magnification = BigDecimal("0.85")
    }
}