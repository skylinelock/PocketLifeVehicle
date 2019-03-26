package dev.sky_lock.mocar.gui;

import dev.sky_lock.glassy.gui.InventoryMenu;
import dev.sky_lock.mocar.gui.contents.ModelSettingContents;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class ModelSettingMenu extends InventoryMenu {

    public ModelSettingMenu(Player holder) {
        super("Edit : ModelSetting", Type.BIG, holder);
        addContents(new ModelSettingContents());
    }
}
