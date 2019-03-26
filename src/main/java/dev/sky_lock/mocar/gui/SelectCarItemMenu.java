package dev.sky_lock.mocar.gui;

import dev.sky_lock.glassy.gui.InventoryMenu;
import dev.sky_lock.mocar.gui.contents.SelectCarItemContents;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class SelectCarItemMenu extends InventoryMenu {

    public SelectCarItemMenu(Player holder) {
        super("Select : CarItem", Type.SMALL, holder);
        addContents(new SelectCarItemContents());
    }
}
