package dev.sky_lock.mocar.gui.contents;

import dev.sky_lock.glassy.gui.MenuContents;
import dev.sky_lock.glassy.gui.Slot;
import dev.sky_lock.mocar.gui.SettingIndex;
import dev.sky_lock.mocar.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class SelectCarItemContents extends MenuContents {

    @Override
    public void open(Player player) {
        addSlot(new Slot(9, ItemStackBuilder.of(Material.WOODEN_HOE, 1).build(), (event) -> {
            setPage(player, SettingIndex.WOODEN_HOE.value());
        }));
        addSlot(new Slot(11, ItemStackBuilder.of(Material.STONE_HOE, 1).build(), (event) -> {
            setPage(player, SettingIndex.STONE_HOE.value());
        }));
        addSlot(new Slot(13, ItemStackBuilder.of(Material.IRON_HOE, 1).build(), (event) -> {
            setPage(player, SettingIndex.IRON_HOE.value());
        }));
        addSlot(new Slot(15, ItemStackBuilder.of(Material.GOLDEN_HOE, 1).build(), (event) -> {
            setPage(player, SettingIndex.GOLDEN_HOE.value());
        }));
        addSlot(new Slot(17, ItemStackBuilder.of(Material.DIAMOND_HOE, 1).build(), (event) -> {
            setPage(player, SettingIndex.DIAMOND_HOE.value());
        }));
    }
}
