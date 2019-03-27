package dev.sky_lock.mocar.gui;

import dev.sky_lock.glassy.gui.InventoryMenu;
import dev.sky_lock.mocar.gui.contents.SelectSpeedContents;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class SelectSpeedMenu extends InventoryMenu {

    public SelectSpeedMenu(Player holder) {
        super("Select : MaxSpeed", Type.BIG, holder);
        addContents(new SelectSpeedContents());
    }
}
