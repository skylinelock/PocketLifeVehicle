package dev.sky_lock.mocar.gui.contents;

import dev.sky_lock.glassy.gui.MenuContents;
import dev.sky_lock.glassy.gui.Slot;
import dev.sky_lock.mocar.gui.EditSessions;
import dev.sky_lock.mocar.gui.SettingIndex;
import dev.sky_lock.mocar.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class SelectCapacityContents extends MenuContents {

    @Override
    public void open(Player player) {
        addSlot(new Slot(20, ItemStackBuilder.of(Material.OAK_PLANKS, 1).name("1").build(), event -> {
            EditSessions.get(player.getUniqueId()).ifPresent(session -> {
                session.setCapacity(1);
                setPage(player, SettingIndex.MAIN_MENU.value());
            });
        }));
        addSlot(new Slot(22, ItemStackBuilder.of(Material.SPRUCE_PLANKS, 1).name("2").build(), event -> {
            EditSessions.get(player.getUniqueId()).ifPresent(session -> {
                session.setCapacity(2);
                setPage(player, SettingIndex.MAIN_MENU.value());
            });
        }));
        addSlot(new Slot(24, ItemStackBuilder.of(Material.BIRCH_PLANKS, 1).name("4").build(), event -> {
            EditSessions.get(player.getUniqueId()).ifPresent(session -> {
                session.setCapacity(4);
                setPage(player, SettingIndex.MAIN_MENU.value());
            });
        }));
    }
}
