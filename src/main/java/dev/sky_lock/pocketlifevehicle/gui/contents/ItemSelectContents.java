package dev.sky_lock.pocketlifevehicle.gui.contents;

import dev.sky_lock.menu.InventoryMenu;
import dev.sky_lock.menu.MenuContents;
import dev.sky_lock.menu.Slot;
import dev.sky_lock.pocketlifevehicle.PLVehicle;
import dev.sky_lock.pocketlifevehicle.gui.EditSessions;
import dev.sky_lock.pocketlifevehicle.gui.ModelMenuIndex;
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author sky_lock
 */

public class ItemSelectContents extends MenuContents {

    public ItemSelectContents(Player player) {
        short idOffset = 1000;
        short k = 1;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                int id = idOffset + k;
                ItemStack item = ItemStackBuilder.of(PLVehicle.CAR_ITEM, 1).customModelData(id).build();
                this.addSlot(new Slot(i * 9 + j, item, (event) -> {
                    EditSessions.of(player.getUniqueId()).ifPresent(session -> {
                        session.setItemType(PLVehicle.CAR_ITEM);
                        session.setItemID(id);
                    });
                    flipPage(player, ModelMenuIndex.ITEM_OPTION.ordinal());
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
