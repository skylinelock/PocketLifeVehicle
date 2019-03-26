package dev.sky_lock.mocar.gui.contents;

import dev.sky_lock.glassy.gui.MenuContents;
import dev.sky_lock.glassy.gui.Slot;
import dev.sky_lock.mocar.gui.EditSessions;
import dev.sky_lock.mocar.gui.ModelSettingMenu;
import dev.sky_lock.mocar.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author sky_lock
 */

public class SelectFuelContents extends MenuContents {

    @Override
    public void open(Player player) {
        int index = 1;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                if (j % 2 != 0) {
                    continue;
                }
                int slot = 9 * i + j;
                int fuel = index * 25;
                ItemStack iron = ItemStackBuilder.of(Material.IRON_BLOCK, 1).name(fuel + "").build();
                addSlot(new Slot(slot, iron, (event) -> {
                    EditSessions.get(player.getUniqueId()).ifPresent(session -> session.setFuel(fuel));
                    new ModelSettingMenu(player).open(player);
                }));
                index += 1;
            }
        }
    }
}
