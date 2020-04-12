package dev.sky_lock.pocketlifevehicle.gui.contents;

import dev.sky_lock.menu.InventoryMenu;
import dev.sky_lock.menu.MenuContents;
import dev.sky_lock.menu.Slot;
import dev.sky_lock.pocketlifevehicle.vehicle.model.Capacity;
import dev.sky_lock.pocketlifevehicle.gui.EditSessions;
import dev.sky_lock.pocketlifevehicle.gui.ModelMenuIndex;
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class CapacityContents extends MenuContents {

    public CapacityContents(Player player) {
        EditSessions.of(player.getUniqueId()).ifPresent(session -> {
            addSlot(new Slot(20, ItemStackBuilder.of(Material.OAK_PLANKS, 1).name("1").build(), event -> {
                session.setCapacity(Capacity.ONE_SEAT);
                flipPage(player, ModelMenuIndex.SETTING.ordinal());
            }));
            addSlot(new Slot(22, ItemStackBuilder.of(Material.SPRUCE_PLANKS, 1).name("2").build(), event -> {
                session.setCapacity(Capacity.TWO_SEATS);
                flipPage(player, ModelMenuIndex.SETTING.ordinal());
            }));
            addSlot(new Slot(24, ItemStackBuilder.of(Material.BIRCH_PLANKS, 1).name("4").build(), event -> {
                session.setCapacity(Capacity.FOR_SEATS);
                flipPage(player, ModelMenuIndex.SETTING.ordinal());
            }));
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
