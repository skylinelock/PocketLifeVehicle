package dev.sky_lock.pocketlifevehicle.vehicle.model

/**
 * @author sky_lock
 */
enum class MaxSpeed(val label: String, val value: Float) {
    TURTLE("亀", 0.20f),
    SLOWEST("最も遅い", 0.30f),
    SLOWER("遅い", 0.40f),
    SLOW("ちょっと遅い", 0.50f),
    NORMAL("普通", 0.60f),
    FAST("ちょっと速い", 0.70f),
    FASTER("速い", 0.80f),
    FASTEST("最も速い", 0.90f),
    GOD("神", 1.0f);

    fun forTick(tick: Int): Float {
        if (tick < 0) {
            return 0.0f
        }
        return this.value * tick
    }
}