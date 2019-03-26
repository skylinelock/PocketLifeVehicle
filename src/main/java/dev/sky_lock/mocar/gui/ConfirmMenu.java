package dev.sky_lock.mocar.gui;

import dev.sky_lock.glassy.gui.InventoryMenu;
import dev.sky_lock.mocar.gui.contents.ConfirmContents;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.util.Consumer;

/**
 * @author sky_lock
 */

public class ConfirmMenu extends InventoryMenu {

    public ConfirmMenu(Player holder, Consumer<InventoryClickEvent> todo) {
        super("Are you sure?", Type.BIG, holder);
        addContents(new ConfirmContents(todo));
    }
}
