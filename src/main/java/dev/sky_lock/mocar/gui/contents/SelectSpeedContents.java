package dev.sky_lock.mocar.gui.contents;

import dev.sky_lock.menu.InventoryMenu;
import dev.sky_lock.menu.MenuContents;
import dev.sky_lock.menu.Slot;
import dev.sky_lock.mocar.car.MaxSpeed;
import dev.sky_lock.mocar.gui.EditSessions;
import dev.sky_lock.mocar.gui.ModelMenuIndex;
import dev.sky_lock.mocar.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author sky_lock
 */

public class SelectSpeedContents extends MenuContents {

    public SelectSpeedContents(Player player) {
        ItemStack speedSelector = ItemStackBuilder.of(Material.SEA_LANTERN, 1).build();
        addSlot(new Slot(11, ItemStackBuilder.of(speedSelector).name(MaxSpeed.SLOWEST.getLabel()).build(), (event) -> {
            setSpeedAndReturn(player, MaxSpeed.SLOWEST);
        }));
        addSlot(new Slot(13, ItemStackBuilder.of(speedSelector).name(MaxSpeed.SLOW.getLabel()).build(), (event) -> {
            setSpeedAndReturn(player, MaxSpeed.SLOW);
        }));
        addSlot(new Slot(15, ItemStackBuilder.of(speedSelector).name(MaxSpeed.NORMAL.getLabel()).build(), (event) -> {
            setSpeedAndReturn(player, MaxSpeed.NORMAL);
        }));
        addSlot(new Slot(29, ItemStackBuilder.of(speedSelector).name(MaxSpeed.FAST.getLabel()).build(), (event) -> {
            setSpeedAndReturn(player, MaxSpeed.FAST);
        }));
        addSlot(new Slot(31, ItemStackBuilder.of(speedSelector).name(MaxSpeed.FASTEST.getLabel()).build(), (event) -> {
            setSpeedAndReturn(player, MaxSpeed.FASTEST);
        }));
    }

    @Override
    public void onFlip(InventoryMenu inventoryMenu) {

    }

    private void setSpeedAndReturn(Player player, MaxSpeed maxSpeed) {
        EditSessions.of(player.getUniqueId()).ifPresent(session -> {
            session.setMaxSpeed(maxSpeed);
            InventoryMenu.of(player).ifPresent(menu -> {
                menu.flip(player, ModelMenuIndex.SETTING.value());
            });
        });
    }
}
