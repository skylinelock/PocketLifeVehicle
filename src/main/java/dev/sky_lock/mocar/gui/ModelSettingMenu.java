package dev.sky_lock.mocar.gui;

import dev.sky_lock.menu.InventoryMenu;
import dev.sky_lock.mocar.gui.contents.*;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class ModelSettingMenu extends InventoryMenu {

    public ModelSettingMenu(Player player) {
        super("Edit : ModelSetting", InventoryMenu.Type.BIG, player);
        addContents(new EditCarModelContents(player));
        addContents(new ModelSettingContents(player));
        addContents(new SelectSpeedContents(player));
        addContents(new SelectCarItemContents(player));
        addContents(new SelectFuelContents(player));
        addContents(new ConfirmContents(player));
        addContents(new SelectCapacityContents(player));
        addContents(new CollideBoxContents(player));
        addContents(new BaseSideContents(player));
        addContents(new CollideHeightContents(player));
    }
}
