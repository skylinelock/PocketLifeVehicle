package dev.sky_lock.mocar.gui;

import dev.sky_lock.glassy.gui.InventoryMenu;
import dev.sky_lock.mocar.gui.contents.EditCarModelContents;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class EditCarModelMenu extends InventoryMenu {

    public EditCarModelMenu(Player holder) {
        super("Edit : CarModel", Type.BIG, holder);
        addContents(new EditCarModelContents());
    }
}