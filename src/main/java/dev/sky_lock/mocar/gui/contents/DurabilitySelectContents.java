package dev.sky_lock.mocar.gui.contents;

import dev.sky_lock.glassy.gui.MenuContents;
import dev.sky_lock.glassy.gui.Slot;
import dev.sky_lock.mocar.car.CarItem;
import dev.sky_lock.mocar.gui.EditSessions;
import dev.sky_lock.mocar.gui.ModelSettingMenu;
import dev.sky_lock.mocar.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author sky_lock
 */

public class DurabilitySelectContents extends MenuContents {
    private final Material hoeType;

    public DurabilitySelectContents(Material hoeType) {
        this.hoeType = hoeType;
    }

    @Override
    public void open(Player player) {
        short damage = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                ItemStack item = ItemStackBuilder.of(hoeType, 1).durability(damage).build();
                final short dmg = damage;
                addSlot(new Slot(i * 9 + j, item, (event) -> {
                    CarItem carItem = new CarItem(hoeType, dmg);
                    EditSessions.get(player.getUniqueId()).ifPresent(session -> {
                        session.setCarItem(carItem);
                    });
                    new ModelSettingMenu(player).open(player);
                }));
                damage += 1;
            }
        }
    }
}