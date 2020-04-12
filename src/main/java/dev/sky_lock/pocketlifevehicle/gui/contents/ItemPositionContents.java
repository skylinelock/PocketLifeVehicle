package dev.sky_lock.pocketlifevehicle.gui.contents;

import dev.sky_lock.menu.InventoryMenu;
import dev.sky_lock.menu.MenuContents;
import dev.sky_lock.menu.Slot;
import dev.sky_lock.pocketlifevehicle.vehicle.model.ItemPosition;
import dev.sky_lock.pocketlifevehicle.gui.EditSessions;
import dev.sky_lock.pocketlifevehicle.gui.ModelMenuIndex;
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author sky_lock
 */

public class ItemPositionContents extends MenuContents {

    public ItemPositionContents(Player player) {
        ItemStack positionSelector = ItemStackBuilder.of(Material.ARMOR_STAND, 1).build();
        addSlot(new Slot(11, ItemStackBuilder.of(positionSelector).name(ItemPosition.HEAD.getLabel()).build(), (event) -> {
            setPositionAndReturn(player, ItemPosition.HEAD);
        }));
        addSlot(new Slot(13, ItemStackBuilder.of(positionSelector).name(ItemPosition.HAND.getLabel()).build(), (event) -> {
            setPositionAndReturn(player, ItemPosition.HAND);
        }));
        addSlot(new Slot(15, ItemStackBuilder.of(positionSelector).name(ItemPosition.CHEST.getLabel()).build(), (event) -> {
            setPositionAndReturn(player, ItemPosition.CHEST);
        }));
        addSlot(new Slot(29, ItemStackBuilder.of(positionSelector).name(ItemPosition.FEET.getLabel()).build(), (event) -> {
            setPositionAndReturn(player, ItemPosition.FEET);
        }));
        addSlot(new Slot(31, ItemStackBuilder.of(positionSelector).name(ItemPosition.LEGS.getLabel()).build(), (event) -> {
            setPositionAndReturn(player, ItemPosition.LEGS);
        }));
    }

    @Override
    public void onFlip(InventoryMenu menu) {

    }

    private void setPositionAndReturn(Player player, ItemPosition position) {
        EditSessions.of(player.getUniqueId()).ifPresent(session -> {
            session.setItemPosition(position);
            InventoryMenu.of(player).ifPresent(menu -> {
                menu.flip(player, ModelMenuIndex.ITEM_OPTION.ordinal());
            });
        });
    }
}
