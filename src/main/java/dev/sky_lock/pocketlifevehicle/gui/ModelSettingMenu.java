package dev.sky_lock.pocketlifevehicle.gui;

import dev.sky_lock.menu.InventoryMenu;
import dev.sky_lock.pocketlifevehicle.gui.contents.*;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class ModelSettingMenu extends InventoryMenu {

    public ModelSettingMenu(Player player) {
        super("モデル設定", InventoryMenu.Type.BIG, player);
        addContents(new EditCarModelContents(player));
        addContents(new ModelSettingContents(player));

        addContents(new FuelContents(player));
        addContents(new SpeedContents(player));
        addContents(new CapacityContents(player));
        addContents(new SteeringLevelContents(player));

        addContents(new ItemOptionContents(player));
        addContents(new ItemSelectContents(player));
        addContents(new ItemPositionContents(player));

        addContents(new CollideBoxContents(player));
        addContents(new BaseSideContents(player));
        addContents(new CollideHeightContents(player));

        addContents(new HeightContents(player));
        addContents(new SoundContents(player));
    }
}
