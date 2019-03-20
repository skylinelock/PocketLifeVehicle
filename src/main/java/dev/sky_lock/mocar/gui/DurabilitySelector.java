package dev.sky_lock.mocar.gui;

import dev.sky_lock.mocar.car.CarItem;
import dev.sky_lock.mocar.gui.api.Button;
import dev.sky_lock.mocar.gui.api.GuiType;
import dev.sky_lock.mocar.gui.api.GuiWindow;
import dev.sky_lock.mocar.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author sky_lock
 */

public class DurabilitySelector extends GuiWindow {

    public DurabilitySelector(Player holder, Material hoeType) {
        super("DurabilitySelector", holder, GuiType.BIG);

        short damage = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                ItemStack item = new ItemStackBuilder(hoeType, 1).durability(damage).build();
                final short dmg = damage;
                super.addComponent(new Button(i * 9 + j, item, (event) -> {
                    CarItem carItem = new CarItem(hoeType, dmg);
                    EditSessions.get(holder.getUniqueId()).setCarItem(carItem);
                    new ModelSetting(holder).open(holder);
                }));
                damage += 1;
            }
        }
    }
}
