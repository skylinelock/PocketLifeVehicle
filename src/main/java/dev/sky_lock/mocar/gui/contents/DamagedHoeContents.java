package dev.sky_lock.mocar.gui.contents;

import dev.sky_lock.glassy.gui.InventoryMenu;
import dev.sky_lock.glassy.gui.MenuContents;
import dev.sky_lock.glassy.gui.Slot;
import dev.sky_lock.mocar.car.CarItem;
import dev.sky_lock.mocar.gui.EditSessions;
import dev.sky_lock.mocar.gui.ModelMenuIndex;
import dev.sky_lock.mocar.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author sky_lock
 */

public class DamagedHoeContents extends MenuContents {
    private final Material hoeType;

    public DamagedHoeContents(Player player, Material type) {
        this.hoeType = type;
        short damage = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                ItemStack item = ItemStackBuilder.of(hoeType, 1).durability(damage).build();
                final short dmg = damage;
                this.addSlot(new Slot(i * 9 + j, item, (event) -> {
                    CarItem carItem = new CarItem(hoeType, dmg);
                    EditSessions.of(player.getUniqueId()).ifPresent(session -> {
                        session.setCarItem(carItem);
                    });
                    flipPage(player, ModelMenuIndex.SETTING.value(), carItem);
                }));
                damage += 1;
            }
        }
    }

    @Override
    public void onFlip(InventoryMenu inventoryMenu) {

    }

    private void flipPage(Player player, int page, final CarItem carItem) {
        InventoryMenu.of(player).ifPresent(menu -> {
            menu.flip(player, page);
        });
    }

}
