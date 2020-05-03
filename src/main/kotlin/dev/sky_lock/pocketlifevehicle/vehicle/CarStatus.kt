package dev.sky_lock.pocketlifevehicle.vehicle

import org.bukkit.Location

/**
 * @author sky_lock
 */
class CarStatus {
    val speed: Speed = Speed()
    var fuel = 0f
    var yaw = 0f
    var isLocked = true
    var isWieldHand = true
    var location: Location? = null

}