package dev.sky_lock.mocar.gui.contents;

import dev.sky_lock.glassy.gui.MenuContents;
import dev.sky_lock.glassy.gui.Slot;
import dev.sky_lock.mocar.car.Speed;
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
        addSlot(new Slot(11, new ItemStackBuilder(speedSelector).name(Speed.SLOWEST.getLabel()).build(), (event) -> {
            setSpeedAndReturn(player, Speed.SLOWEST);
        }));
        addSlot(new Slot(13, new ItemStackBuilder(speedSelector).name(Speed.SLOW.getLabel()).build(), (event) -> {
            setSpeedAndReturn(player, Speed.SLOW);
        }));
        addSlot(new Slot(15, new ItemStackBuilder(speedSelector).name(Speed.NORMAL.getLabel()).build(), (event) -> {
            setSpeedAndReturn(player, Speed.NORMAL);
        }));
        addSlot(new Slot(29, new ItemStackBuilder(speedSelector).name(Speed.FAST.getLabel()).build(), (event) -> {
            setSpeedAndReturn(player, Speed.FAST);
        }));
        addSlot(new Slot(31, new ItemStackBuilder(speedSelector).name(Speed.FASTEST.getLabel()).build(), (event) -> {
            setSpeedAndReturn(player, Speed.FASTEST);
        }));
    }

    private void setSpeedAndReturn(Player player, Speed speed) {
        EditSessions.get(player.getUniqueId()).ifPresent(session -> {
            session.setSpeed(speed);
            new ModelSettingMenu(player).open(player);
        });
    }
}
