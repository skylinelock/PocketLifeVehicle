package dev.sky_lock.pocketlifevehicle.vehicle.entity

import dev.sky_lock.pocketlifevehicle.ext.kotlin.truncateToOneDecimalPlace
import dev.sky_lock.pocketlifevehicle.text.Line
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.Bukkit
import org.bukkit.Location
import java.util.*
import java.util.stream.IntStream
import kotlin.math.abs
import kotlin.math.roundToInt

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

    fun meterPanelLine(): Line {
        val speed = engine.speed
        val line = Line()

        if (!model.flag.eventOnly) {
            val fuelRate = tank.fuel / model.spec.maxFuel
            val filled = (70 * fuelRate).roundToInt()

            line.redBold("E ")
            IntStream.range(0, filled).forEach { line.green("ǀ") }
            IntStream.range(0, 70 - filled).forEach { line.red("ǀ") }
            line.greenBold(" F   ")
            if (speed.isApproximateZero) {
                line.darkPurpleBold("P   ")
            } else {
                if (speed.isPositive) {
                    line.darkPurpleBold("D   ")
                } else if (speed.isNegative) {
                    line.darkPurpleBold("R   ")
                }
            }
        }
        val blockPerSecond = abs(engine.speedPerSecond()).truncateToOneDecimalPlace()
        line.darkGreenBold(blockPerSecond).grayBold(" blocks/s")
        return line
    }
}