package dev.sky_lock.pocketlifevehicle.vehicle

import org.bukkit.Location

/**
 * @author sky_lock
 */
class State {
    val speed = Speed()
    var fuel = 0f
    var yaw = 0f
    var isLocked = true
    var shouldPlaySound = true
    var shouldAnimate = true

    var location: Location? = null
}