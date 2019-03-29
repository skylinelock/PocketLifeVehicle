package dev.sky_lock.mocar.gui;

import dev.sky_lock.glassy.gui.InventoryMenu;
import dev.sky_lock.mocar.gui.contents.*;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class ModelSettingMenu extends InventoryMenu {

    public ModelSettingMenu(Player holder) {
        super("Edit : ModelSetting", Type.BIG, holder);
        addContents(new ModelSettingContents());
        addContents(new SelectSpeedContents());
        addContents(new SelectCarItemContents());
        addContents(new SelectFuelContents());
        addContents(new WoodenHoeContents());
        addContents(new StoneHoeContents());
        addContents(new IronHoeContents());
        addContents(new GoldenHoeContents());
        addContents(new DiamondHoeContents());
        addContents(new ConfirmContents());
        addContents(new SelectCapacityContents());
    }
}
