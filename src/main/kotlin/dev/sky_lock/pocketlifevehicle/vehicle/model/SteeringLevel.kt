package dev.sky_lock.pocketlifevehicle.vehicle.model

/**
 * @author sky_lock
 */

enum class SteeringLevel(val label: String, val value: Float) {
    STRAIGHT("直線番長", 2.0F),
    LOWEST("最も切りづらい", 3.0F),
    LOWER("切りづらい", 4.0F),
    LOW("ちょっと切りづらい", 5.0F),
    NORMAL("普通", 6.0F),
    HIGH("ちょっと切りやすい", 7.0F),
    HIGHER("切りやすい", 8.0F),
    HIGHEST("最も切りやすい", 9.0F),
    GOD("神", 10.0F);
}