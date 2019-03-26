package dev.sky_lock.mocar.gui.contents;

import dev.sky_lock.glassy.gui.MenuContents;
import dev.sky_lock.glassy.gui.Slot;
import dev.sky_lock.mocar.gui.SelectDurabilityMenu;
import dev.sky_lock.mocar.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class SelectCarItemContents extends MenuContents {

    @Override
    public void open(Player player) {
        addSlot(new Slot(9, ItemStackBuilder.of(Material.WOOD_HOE, 1).build(), (event) -> {
            new SelectDurabilityMenu(player, Material.WOOD_HOE).open(player);
        }));
        addSlot(new Slot(11, ItemStackBuilder.of(Material.STONE_HOE, 1).build(), (event) -> {
            new SelectDurabilityMenu(player, Material.STONE).open(player);
        }));
        addSlot(new Slot(13, ItemStackBuilder.of(Material.IRON_HOE, 1).build(), (event) -> {
            new SelectDurabilityMenu(player, Material.IRON_HOE).open(player);
        }));
        addSlot(new Slot(15, ItemStackBuilder.of(Material.GOLD_HOE, 1).build(), (event) -> {
            new SelectDurabilityMenu(player, Material.GOLD_HOE).open(player);
        }));
        addSlot(new Slot(17, ItemStackBuilder.of(Material.DIAMOND_HOE, 1).build(), (event) -> {
            new SelectDurabilityMenu(player, Material.DIAMOND_HOE).open(player);
        }));
    }
}
