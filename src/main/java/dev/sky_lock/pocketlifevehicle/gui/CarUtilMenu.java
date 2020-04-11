package dev.sky_lock.pocketlifevehicle.gui;

import dev.sky_lock.menu.InventoryMenu;
import dev.sky_lock.pocketlifevehicle.vehicle.Car;
import dev.sky_lock.pocketlifevehicle.gui.contents.CarUtilContents;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class CarUtilMenu extends InventoryMenu {

    public CarUtilMenu(Player holder, Car car) {
        super("ユーティリティ", Type.BIG, holder);
        addContents(new CarUtilContents(car));
    }
}
