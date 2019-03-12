package dev.sky_lock.mocar.gui;

import dev.sky_lock.mocar.car.Car;
import dev.sky_lock.mocar.gui.api.Gage;
import dev.sky_lock.mocar.gui.api.Button;
import dev.sky_lock.mocar.gui.api.GuiWindow;
import dev.sky_lock.mocar.util.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collections;

/**
 * @author sky_lock
 */

public class CarEntityUtility extends GuiWindow {

    public CarEntityUtility(Player player, Car car) {
        super("CarUtility", player);
        super.addComponent(new Gage(45, 53, new ItemStackBuilder(Material.STAINED_GLASS_PANE, 1).dyeColor(DyeColor.RED).build(),
                new ItemStackBuilder(Material.STAINED_GLASS_PANE, 1).dyeColor(DyeColor.GREEN).build()));
/*        super.addComponent(new Panel(Arrays.asList(15, 16, 24, 25, 33, 34), new ItemStackBuilder(Material.STAINED_GLASS_PANE, 1).dyeColor(DyeColor.BLUE).build(), (event) -> {
            event.getWhoClicked().sendMessage("hogehoge");
        }));*/
        super.addComponent(new Button(4, new ItemStackBuilder(Material.MINECART, 1).name(ChatColor.GREEN + "レッカー移動").lore(Collections.singletonList("アイテム化して持ち運べるようにします")).build(), (event) -> {
            car.tow();
            close(player);
        }));
    }
}
