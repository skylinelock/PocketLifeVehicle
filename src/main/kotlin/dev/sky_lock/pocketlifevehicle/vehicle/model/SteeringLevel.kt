package dev.sky_lock.pocketlifevehicle.vehicle.model

/**
 * @author sky_lock
 */

enum class SteeringLevel(val label: String, val value: Float) {
    LOWEST("最も切り辛い", 1.0F),
    LOW("切り辛い", 2.5F),
    NORMAL("普通", 4.0F),
    HIGH("切りやすい", 6.5F),
    HIGHEST("最も切りやすい", 8.0F);
}