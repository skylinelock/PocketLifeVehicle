package dev.sky_lock.mocar.gui.contents;

import dev.sky_lock.glassy.gui.InventoryMenu;
import dev.sky_lock.glassy.gui.MenuContents;
import dev.sky_lock.glassy.gui.Slot;
import dev.sky_lock.mocar.gui.ModelMenuIndex;
import dev.sky_lock.mocar.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class SelectCarItemContents extends MenuContents {

    public SelectCarItemContents(Player player) {
        addSlot(new Slot(9, ItemStackBuilder.of(Material.WOODEN_HOE, 1).build(), (event) -> {
            flipPage(player, ModelMenuIndex.WOODEN_HOE.value());
        }));
        addSlot(new Slot(11, ItemStackBuilder.of(Material.STONE_HOE, 1).build(), (event) -> {
            flipPage(player, ModelMenuIndex.STONE_HOE.value());
        }));
        addSlot(new Slot(13, ItemStackBuilder.of(Material.IRON_HOE, 1).build(), (event) -> {
            flipPage(player, ModelMenuIndex.IRON_HOE.value());
        }));
        addSlot(new Slot(15, ItemStackBuilder.of(Material.GOLDEN_HOE, 1).build(), (event) -> {
            flipPage(player, ModelMenuIndex.GOLDEN_HOE.value());
        }));
        addSlot(new Slot(17, ItemStackBuilder.of(Material.DIAMOND_HOE, 1).build(), (event) -> {
            flipPage(player, ModelMenuIndex.DIAMOND_HOE.value());
        }));
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
