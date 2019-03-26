package dev.sky_lock.mocar.gui;

import dev.sky_lock.glassy.gui.InventoryMenu;
import dev.sky_lock.mocar.gui.contents.DurabilitySelectContents;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class SelectDurabilityMenu extends InventoryMenu {

    public SelectDurabilityMenu(Player holder, Material hoeType) {
        super("Select : Durability", Type.BIG, holder);
        addContents(new DurabilitySelectContents(hoeType));
    }
}
