package dev.sky_lock.pocketlifevehicle.vehicle.model

/**
 * @author sky_lock
 */
enum class MaxSpeed(val label: String, val max: Float) {
    SLOWEST("最も遅い", 0.20f),
    SLOW("遅い", 0.30f),
    NORMAL("普通", 0.40f),
    FAST("速い", 0.50f),
    FASTEST("最も速い", 0.60f);
}