package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.Location
import java.util.function.Consumer

/**
 * @author sky_lock
 */
class OneSeatVehicle(model: Model) : Vehicle(model) {
    override fun spawn(location: Location) {
        super.spawn(location)
        val driver = SeatArmorStand(center!!.getWorld(), location.x, location.y, location.z)
        driver.assemble(this, SeatPosition.ONE_DRIVER)
        addSeat(driver)
        super.seats.forEach(Consumer { entity: SeatArmorStand -> center!!.getWorld().addEntity(entity) })
    }
}