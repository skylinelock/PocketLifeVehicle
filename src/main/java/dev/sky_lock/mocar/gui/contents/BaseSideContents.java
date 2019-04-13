package dev.sky_lock.mocar.gui.contents;

import dev.sky_lock.glassy.gui.InventoryMenu;
import dev.sky_lock.glassy.gui.MenuContents;
import dev.sky_lock.glassy.gui.Slot;
import dev.sky_lock.mocar.gui.EditSessions;
import dev.sky_lock.mocar.gui.ModelMenuIndex;
import dev.sky_lock.mocar.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author sky_lock
 */

public class BaseSideContents extends MenuContents {

    public BaseSideContents(Player player) {
        for (int i = 1; i < 10; i ++) {
            final float baseSide = i * 0.5F;
            ItemStack baseSideItem = ItemStackBuilder.of(Material.LIGHT_BLUE_CONCRETE, 1).name(String.valueOf(baseSide)).build();
            this.addSlot(new Slot(17 + i, baseSideItem, event -> {
                EditSessions.of(player.getUniqueId()).ifPresent(session -> {
                    session.setCollideBaseSide(baseSide);
                });
                InventoryMenu.of(player).ifPresent(menu -> menu.flip(player, ModelMenuIndex.COLLIDE_BOX.value()));
            }));
        }
    }

    @Override
    public void onFlip(InventoryMenu inventoryMenu) {

    }
}
