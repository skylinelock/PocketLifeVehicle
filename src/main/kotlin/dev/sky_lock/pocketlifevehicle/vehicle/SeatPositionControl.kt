package dev.sky_lock.pocketlifevehicle.vehicle

import org.bukkit.Location

/**
 * @author sky_lock
 */
internal class SeatPositionControl {
    fun calculate(location: Location, position: SeatPosition): Location {
        val vector = location.direction
        when (position) {
            SeatPosition.ONE_DRIVER -> return location
            SeatPosition.TWO_DRIVER -> {
                vector.multiply(1)
                return location.clone().add(vector)
            }
            SeatPosition.TWO_PASSENGER -> {
                vector.multiply(1).rotateAroundY(Math.toRadians(180.0))
                return location.clone().add(vector)
            }
            SeatPosition.FOUR_DRIVER -> {
                vector.multiply(0.5).rotateAroundY(Math.toRadians(-90.0))
                return location.clone().add(vector)
            }
            SeatPosition.FOUR_PASSENGER -> {
                vector.multiply(0.5).rotateAroundY(Math.toRadians(90.0))
                return location.clone().add(vector)
            }
            SeatPosition.FOUR_REAR_LEFT -> {
                vector.multiply(1.3).rotateAroundY(Math.toRadians(158.0))
                return location.clone().add(vector)
            }
            SeatPosition.FOUR_REAR_RIGHT -> {
                vector.multiply(1.3).rotateAroundY(Math.toRadians(-158.0))
                return location.clone().add(vector)
            }
        }
    }
}