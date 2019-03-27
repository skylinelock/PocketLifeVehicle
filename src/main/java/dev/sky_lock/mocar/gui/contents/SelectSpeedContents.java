package dev.sky_lock.mocar.gui.contents;

import dev.sky_lock.glassy.gui.MenuContents;
import dev.sky_lock.glassy.gui.Slot;
import dev.sky_lock.mocar.car.MaxSpeed;
import dev.sky_lock.mocar.gui.EditSessions;
import dev.sky_lock.mocar.gui.ModelSettingMenu;
import dev.sky_lock.mocar.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author sky_lock
 */

public class SelectSpeedContents extends MenuContents {

    @Override
    public void open(Player player) {
        ItemStack speedSelector = ItemStackBuilder.of(Material.SEA_LANTERN, 1).build();
        addSlot(new Slot(11, new ItemStackBuilder(speedSelector).name(MaxSpeed.SLOWEST.getLabel()).build(), (event) -> {
            setSpeedAndReturn(player, MaxSpeed.SLOWEST);
        }));
        addSlot(new Slot(13, new ItemStackBuilder(speedSelector).name(MaxSpeed.SLOW.getLabel()).build(), (event) -> {
            setSpeedAndReturn(player, MaxSpeed.SLOW);
        }));
        addSlot(new Slot(15, new ItemStackBuilder(speedSelector).name(MaxSpeed.NORMAL.getLabel()).build(), (event) -> {
            setSpeedAndReturn(player, MaxSpeed.NORMAL);
        }));
        addSlot(new Slot(29, new ItemStackBuilder(speedSelector).name(MaxSpeed.FAST.getLabel()).build(), (event) -> {
            setSpeedAndReturn(player, MaxSpeed.FAST);
        }));
        addSlot(new Slot(31, new ItemStackBuilder(speedSelector).name(MaxSpeed.FASTEST.getLabel()).build(), (event) -> {
            setSpeedAndReturn(player, MaxSpeed.FASTEST);
        }));
    }

    private void setSpeedAndReturn(Player player, MaxSpeed maxSpeed) {
        EditSessions.get(player.getUniqueId()).ifPresent(session -> {
            session.setMaxSpeed(maxSpeed);
            new ModelSettingMenu(player).open(player);
        });
    }
}
