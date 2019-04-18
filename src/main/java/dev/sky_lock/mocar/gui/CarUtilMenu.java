package dev.sky_lock.mocar.gui;

import dev.sky_lock.menu.InventoryMenu;
import dev.sky_lock.mocar.car.Car;
import dev.sky_lock.mocar.gui.contents.CarUtilContents;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class CarUtilMenu extends InventoryMenu {

    public CarUtilMenu(Player holder, Car car) {
        super("CarUtility", Type.BIG, holder);
        addContents(new CarUtilContents(car));
    }
}
