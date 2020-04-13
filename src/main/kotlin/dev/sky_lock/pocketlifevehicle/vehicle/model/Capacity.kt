package dev.sky_lock.pocketlifevehicle.vehicle.model

import java.util.*

/**
 * @author sky_lock
 */
enum class Capacity(private val value: Int) {
    ONE_SEAT(1), TWO_SEATS(2), FOR_SEATS(4);

    fun value(): Int {
        return value
    }

    companion object {
        @JvmStatic
        fun valueOf(value: Int): Capacity {
            return Arrays.stream(values()).filter { capacity: Capacity -> capacity.value() == value }.findFirst().orElse(null)
        }
    }

}