package dev.sky_lock.mocar.gui.contents;

import dev.sky_lock.menu.InventoryMenu;
import dev.sky_lock.menu.MenuContents;
import dev.sky_lock.menu.Slot;
import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.CarItem;
import dev.sky_lock.mocar.gui.EditSessions;
import dev.sky_lock.mocar.gui.ModelMenuIndex;
import dev.sky_lock.mocar.item.ItemStackBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author sky_lock
 */

public class SelectCarItemContents extends MenuContents {

    public SelectCarItemContents(Player player) {
        short idOffset = 1000;
        short k = 1;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                int modelId = idOffset + k;
                ItemStack item = ItemStackBuilder.of(MoCar.CAR_ITEM, 1).customModelData(idOffset + modelId).build();
                this.addSlot(new Slot(i * 9 + j, item, (event) -> {
                    CarItem carItem = new CarItem(MoCar.CAR_ITEM, modelId);
                    EditSessions.of(player.getUniqueId()).ifPresent(session -> {
                        session.setCarItem(carItem);
                    });
                    flipPage(player, ModelMenuIndex.SETTING.value());
                }));
                k++;
            }
        }
    }

    @Override
    public void onFlip(InventoryMenu inventoryMenu) {

    }

    private void flipPage(Player player, int page) {
        InventoryMenu.of(player).ifPresent(menu -> {
            menu.flip(player, page);
        });
    }

}
