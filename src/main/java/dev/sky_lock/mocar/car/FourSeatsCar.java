package dev.sky_lock.mocar.car;

import org.bukkit.Location;

/**
 * @author sky_lock
 */

class FourSeatsCar extends Car {

    FourSeatsCar(CarModel model) {
        super(model);
    }

    @Override
    void spawn(Location location) {
        super.spawn(location);
        SeatArmorStand driver = new SeatArmorStand(getCenter().getWorld());
        driver.assemble(this, SeatPosition.FOUR_DRIVER);
        SeatArmorStand passenger = new SeatArmorStand(getCenter().getWorld());
        passenger.assemble(this, SeatPosition.FOUR_PASSENGER);
        SeatArmorStand rearRight = new SeatArmorStand(getCenter().getWorld());
        rearRight.assemble(this, SeatPosition.FOUR_REAR_RIGHT);
        SeatArmorStand rearLeft = new SeatArmorStand(getCenter().getWorld());
        rearLeft.assemble(this, SeatPosition.FOUR_REAR_LEFT);
        addSeat(driver);
        addSeat(passenger);
        addSeat(rearRight);
        addSeat(rearLeft);
        super.seats.forEach(getCenter().getWorld()::addEntity);
    }
}
