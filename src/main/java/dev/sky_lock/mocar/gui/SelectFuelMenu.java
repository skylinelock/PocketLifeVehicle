package dev.sky_lock.mocar.gui;

import dev.sky_lock.glassy.gui.InventoryMenu;
import dev.sky_lock.mocar.gui.contents.SelectFuelContents;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class SelectFuelMenu extends InventoryMenu {

    public SelectFuelMenu(Player holder) {
        super("Select : Fuel", Type.BIG, holder);
        addContents(new SelectFuelContents());
    }
}
