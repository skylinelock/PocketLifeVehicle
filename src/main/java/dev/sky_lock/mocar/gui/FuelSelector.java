package dev.sky_lock.mocar.gui;

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

public class FuelSelector extends GuiWindow {

    public FuelSelector(Player holder) {
        super("FuelSelector", holder, GuiType.BIG);

        int index = 1;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                if (j % 2 != 0) {
                    continue;
                }
                int slot = 9 * i + j;
                int fuel = index * 25;
                ItemStack iron = new ItemStackBuilder(Material.IRON_BLOCK, 1).name(fuel + "").build();
                super.addComponent(new Button(slot, iron, (event) -> {
                    EditSessions.get(holder.getUniqueId()).setFuel(fuel);
                    new ModelSetting(holder).open(holder);
                }));
                index += 1;
            }
        }
    }

}
