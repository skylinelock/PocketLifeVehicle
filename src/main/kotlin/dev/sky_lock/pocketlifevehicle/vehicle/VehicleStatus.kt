package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.Bukkit
import org.bukkit.Location
import java.util.*

/**
 * @author sky_lock
 */
class VehicleStatus(val owner: UUID?, var location: Location, val model: Model, fuel: Float) {
    val ownerName: String
        get() {
            val uuid = owner ?: return "unknown"
            return Bukkit.getOfflinePlayer(uuid).name ?: "unknown"
        }

    val tank = FuelTank(fuel, model.spec.maxFuel)
    val engine = Engine(tank, model)
    val steering = Steering(this)

    var yaw = location.yaw

    private var isEventOnly = model.flag.eventOnly
    var isLocked = !isEventOnly
    var shouldPlaySound = model.flag.engineSound
    var shouldAnimate = model.flag.animation
    var isLoaded = true
    var isUndrivable = false

    fun updateLocation(location: Location) {
        this.location = location
    }
}