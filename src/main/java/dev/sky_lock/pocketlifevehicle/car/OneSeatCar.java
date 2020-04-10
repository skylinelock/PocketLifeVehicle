package dev.sky_lock.pocketlifevehicle.car;

import org.bukkit.Location;

/**
 * @author sky_lock
 */

class OneSeatCar extends Car {

    OneSeatCar(CarModel model) {
        super(model);
    }

    @Override
    void spawn(Location location) {
        super.spawn(location);
        SeatArmorStand driver = new SeatArmorStand(getCenter().getWorld(), location.getX(), location.getY(), location.getZ());
        driver.assemble(this, SeatPosition.ONE_DRIVER);
        addSeat(driver);
        super.seats.forEach(getCenter().getWorld()::addEntity);
    }
}
