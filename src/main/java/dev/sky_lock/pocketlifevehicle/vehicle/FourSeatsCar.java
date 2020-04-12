package dev.sky_lock.pocketlifevehicle.vehicle;

import dev.sky_lock.pocketlifevehicle.vehicle.model.Model;
import org.bukkit.Location;

/**
 * @author sky_lock
 */

class FourSeatsCar extends Car {

    FourSeatsCar(Model model) {
        super(model);
    }

    @Override
    void spawn(Location location) {
        super.spawn(location);
        SeatArmorStand driver = new SeatArmorStand(getCenter().getWorld(), location.getX(), location.getY(), location.getZ());
        driver.assemble(this, SeatPosition.FOUR_DRIVER);
        SeatArmorStand passenger = new SeatArmorStand(getCenter().getWorld(), location.getX(), location.getY(), location.getZ());
        passenger.assemble(this, SeatPosition.FOUR_PASSENGER);
        SeatArmorStand rearRight = new SeatArmorStand(getCenter().getWorld(), location.getX(), location.getY(), location.getZ());
        rearRight.assemble(this, SeatPosition.FOUR_REAR_RIGHT);
        SeatArmorStand rearLeft = new SeatArmorStand(getCenter().getWorld(), location.getX(), location.getY(), location.getZ());
        rearLeft.assemble(this, SeatPosition.FOUR_REAR_LEFT);
        addSeat(driver);
        addSeat(passenger);
        addSeat(rearRight);
        addSeat(rearLeft);
        super.seats.forEach(getCenter().getWorld()::addEntity);
    }
}