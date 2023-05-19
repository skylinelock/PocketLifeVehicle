package dev.sky_lock.pocketlifevehicle.vehicle.entity

/**
 * @author sky_lock
 */

class FuelTank(var fuel: Float, private val max: Float) {

    fun consume() {
        if (fuel > 0.05F) {
            fuel -= 0.05F
        } else {
            fuel = 0.0F
        }
    }

    fun refuel(diff: Float): Boolean {
        if (fuel >= max) {
            return false
        } else if (fuel + diff >= max) {
            max()
            return true
        }
        fuel += diff
        return true
    }

    private fun max() {
        fuel = max
    }

    fun isEmpty(): Boolean {
        return fuel <= 0.0F
    }
}