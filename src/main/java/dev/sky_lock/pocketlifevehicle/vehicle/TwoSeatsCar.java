package dev.sky_lock.pocketlifevehicle.vehicle;

import dev.sky_lock.pocketlifevehicle.vehicle.model.Model;
import org.bukkit.Location;

/**
 * @author sky_lock
 */

class TwoSeatsCar extends Car {

    TwoSeatsCar(Model model) {
        super(model);
    }

    @Override
    void spawn(Location location) {
        super.spawn(location);
        SeatArmorStand driver = new SeatArmorStand(getCenter().getWorld(), location.getX(), location.getY(), location.getZ());
        driver.assemble(this, SeatPosition.TWO_DRIVER);
        SeatArmorStand passenger = new SeatArmorStand(getCenter().getWorld(), location.getX(), location.getY(), location.getZ());
        passenger.assemble(this, SeatPosition.TWO_PASSENGER);
        addSeat(driver);
        addSeat(passenger);
        super.seats.forEach(getCenter().getWorld()::addEntity);
    }
}
