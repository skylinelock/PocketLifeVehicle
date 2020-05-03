package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.Location
import java.util.function.Consumer

/**
 * @author sky_lock
 */
class TwoSeatsVehicle(model: Model) : Vehicle(model) {
    override fun spawn(location: Location) {
        super.spawn(location)
        val driver = SeatArmorStand(center!!.getWorld(), location.x, location.y, location.z)
        driver.assemble(this, SeatPosition.TWO_DRIVER)
        val passenger = SeatArmorStand(center!!.getWorld(), location.x, location.y, location.z)
        passenger.assemble(this, SeatPosition.TWO_PASSENGER)
        addSeat(driver)
        addSeat(passenger)
        super.seats.forEach(Consumer { entity: SeatArmorStand -> center!!.getWorld().addEntity(entity) })
    }
}