package dev.sky_lock.mocar.gui;

import dev.sky_lock.mocar.gui.api.GuiType;
import dev.sky_lock.mocar.gui.api.GuiWindow;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class FuelSelector extends GuiWindow {

    public FuelSelector(Player holder) {
        super("FuelSelector", holder, GuiType.WIDE);
    }

}
