package dev.sky_lock.pocketlifevehicle.gui.contents;

import dev.sky_lock.menu.InventoryMenu;
import dev.sky_lock.menu.MenuContents;
import dev.sky_lock.menu.Slot;
import dev.sky_lock.pocketlifevehicle.car.ModelPosition;
import dev.sky_lock.pocketlifevehicle.gui.EditSessions;
import dev.sky_lock.pocketlifevehicle.gui.ModelMenuIndex;
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author sky_lock
 */

public class SelectPositionContents extends MenuContents {

    public SelectPositionContents(Player player) {
        ItemStack positionSelector = ItemStackBuilder.of(Material.ARMOR_STAND, 1).build();
        addSlot(new Slot(11, ItemStackBuilder.of(positionSelector).name(ModelPosition.HEAD.getLabel()).build(), (event) -> {
            setPositionAndReturn(player, ModelPosition.HEAD);
        }));
        addSlot(new Slot(13, ItemStackBuilder.of(positionSelector).name(ModelPosition.HAND.getLabel()).build(), (event) -> {
            setPositionAndReturn(player, ModelPosition.HAND);
        }));
        addSlot(new Slot(15, ItemStackBuilder.of(positionSelector).name(ModelPosition.CHEST.getLabel()).build(), (event) -> {
            setPositionAndReturn(player, ModelPosition.CHEST);
        }));
        addSlot(new Slot(29, ItemStackBuilder.of(positionSelector).name(ModelPosition.FEET.getLabel()).build(), (event) -> {
            setPositionAndReturn(player, ModelPosition.FEET);
        }));
        addSlot(new Slot(31, ItemStackBuilder.of(positionSelector).name(ModelPosition.LEGS.getLabel()).build(), (event) -> {
            setPositionAndReturn(player, ModelPosition.LEGS);
        }));
    }

    @Override
    public void onFlip(InventoryMenu menu) {

    }

    private void setPositionAndReturn(Player player, ModelPosition position) {
        EditSessions.of(player.getUniqueId()).ifPresent(session -> {
            session.setPosition(position);
            InventoryMenu.of(player).ifPresent(menu -> {
                menu.flip(player, ModelMenuIndex.SETTING.value());
            });
        });
    }
}
