package dev.sky_lock.pocketlifevehicle.vehicle

import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.Location
import java.util.function.Consumer

/**
 * @author sky_lock
 */
internal class FourSeatsCar(model: Model?) : Car(model!!) {
    override fun spawn(location: Location) {
        super.spawn(location)
        val driver = SeatArmorStand(center!!.getWorld(), location.x, location.y, location.z)
        driver.assemble(this, SeatPosition.FOUR_DRIVER)
        val passenger = SeatArmorStand(center!!.getWorld(), location.x, location.y, location.z)
        passenger.assemble(this, SeatPosition.FOUR_PASSENGER)
        val rearRight = SeatArmorStand(center!!.getWorld(), location.x, location.y, location.z)
        rearRight.assemble(this, SeatPosition.FOUR_REAR_RIGHT)
        val rearLeft = SeatArmorStand(center!!.getWorld(), location.x, location.y, location.z)
        rearLeft.assemble(this, SeatPosition.FOUR_REAR_LEFT)
        addSeat(driver)
        addSeat(passenger)
        addSeat(rearRight)
        addSeat(rearLeft)
        super.seats.forEach(Consumer { entity: SeatArmorStand? -> center!!.getWorld().addEntity(entity) })
    }
}