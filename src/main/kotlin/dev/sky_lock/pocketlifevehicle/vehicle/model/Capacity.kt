package dev.sky_lock.pocketlifevehicle.vehicle.model


/**
 * @author sky_lock
 */
enum class Capacity(private val value: Int) {
    ONE_SEAT(1), TWO_SEATS(2), FOUR_SEATS(4);

    fun value(): Int {
        return value
    }

    companion object {
        fun valueOf(value: Int): Capacity {
            return values().find { capacity: Capacity -> capacity.value() == value } ?: ONE_SEAT
        }
    }

}