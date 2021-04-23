package dev.sky_lock.pocketlifevehicle.vehicle.model


/**
 * @author sky_lock
 */
enum class Capacity(private val value: Int) {
    SINGLE(1),
    DOUBLE(2),
    QUAD(4);

    fun value(): Int = value

    companion object {
        fun valueOf(value: Int): Capacity {
            return values().find { capacity -> capacity.value() == value } ?: SINGLE
        }
    }

}