package dev.sky_lock.mocar.gui;

import dev.sky_lock.glassy.gui.InventoryMenu;
import dev.sky_lock.mocar.car.CarArmorStand;
import dev.sky_lock.mocar.gui.contents.CarUtilContents;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class CarUtilMenu extends InventoryMenu {

    public CarUtilMenu(Player holder, CarArmorStand car) {
        super("CarUtility", Type.BIG, holder);
        addContents(new CarUtilContents(car));
    }
}
