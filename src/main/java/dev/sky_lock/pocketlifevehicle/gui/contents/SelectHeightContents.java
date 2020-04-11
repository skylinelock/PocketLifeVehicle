package dev.sky_lock.pocketlifevehicle.gui.contents;

import dev.sky_lock.menu.InventoryMenu;
import dev.sky_lock.menu.MenuContents;
import dev.sky_lock.menu.Slot;
import dev.sky_lock.pocketlifevehicle.gui.EditSessions;
import dev.sky_lock.pocketlifevehicle.gui.ModelMenuIndex;
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder;
import dev.sky_lock.pocketlifevehicle.util.Formats;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author sky_lock
 */

public class SelectHeightContents extends MenuContents {

    public SelectHeightContents(Player player) {
        EditSessions.of(player.getUniqueId()).ifPresent(session -> {
            for (int i = 0; i < 18; i++) {
                float height = 0.1F * (i + 1);
                ItemStack item = ItemStackBuilder.of(Material.WHITE_STAINED_GLASS, 1).name(Formats.truncateToOneDecimalPlace(height)).build();
                addSlot(new Slot(9 * 2 + i, item, event -> {
                    session.setHeight(height);
                    flipPage(player, ModelMenuIndex.SETTING.value());
                }));
            }
        });
    }

    @Override
    public void onFlip(InventoryMenu menu) {

    }

    private void flipPage(Player player, int page) {
        InventoryMenu.of(player).ifPresent(menu -> {
            menu.flip(player, page);
        });
    }
}
