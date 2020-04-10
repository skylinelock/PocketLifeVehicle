package dev.sky_lock.pocketlifevehicle.gui.contents;

import dev.sky_lock.menu.InventoryMenu;
import dev.sky_lock.menu.MenuContents;
import dev.sky_lock.menu.Slot;
import dev.sky_lock.pocketlifevehicle.gui.EditSessions;
import dev.sky_lock.pocketlifevehicle.gui.ModelMenuIndex;
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author sky_lock
 */

public class SelectFuelContents extends MenuContents {

    public SelectFuelContents(Player player) {
        EditSessions.of(player.getUniqueId()).ifPresent(session -> {
            int index = 1;
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 9; j++) {
                    if (j % 2 != 0) {
                        continue;
                    }
                    int slot = 9 * i + j;
                    int fuel = index * 25;
                    ItemStack iron = ItemStackBuilder.of(Material.IRON_BLOCK, 1).name(fuel + "").build();
                    addSlot(new Slot(slot, iron, event -> {
                        session.setFuel(fuel);
                        flipPage(player, ModelMenuIndex.SETTING.value());
                    }));
                    index += 1;
                }
            }
        });
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
