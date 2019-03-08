package dev.sky_lock.mocar.gui;

import dev.sky_lock.mocar.util.ItemStackBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author sky_lock
 */

public class CarUtilityGui extends GuiWindow {

    public CarUtilityGui(Player player) {
        super("CarUtility", player);
        super.addComponent(new GuiGage(45, 53, new ItemStackBuilder(Material.STAINED_GLASS_PANE, 1).dyeColor(DyeColor.RED).build(),
                new ItemStackBuilder(Material.STAINED_GLASS_PANE, 1).dyeColor(DyeColor.GREEN).build()));
        super.addComponent(new GuiPanel(Arrays.asList(15, 16, 24, 25, 33, 34), new ItemStackBuilder(Material.STAINED_GLASS_PANE, 1).dyeColor(DyeColor.BLUE).build(), (event) -> {
            event.getWhoClicked().sendMessage("hogehoge");
        }));
        super.addComponent(new GuiItem(4, new ItemStackBuilder(Material.MINECART, 1).build(), (event) -> {

        }));
        super.open();
    }
}
