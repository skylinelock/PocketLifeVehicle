package dev.sky_lock.mocar.gui;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.Car;
import dev.sky_lock.mocar.util.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author sky_lock
 */

public class CarUtilityGui extends GuiWindow {
    public static final List<GuiWindow> windows = new ArrayList<>();

    public CarUtilityGui(Player player, Car car) {
        super("CarUtility", player);
        super.addComponent(new GuiGage(45, 53, new ItemStackBuilder(Material.STAINED_GLASS_PANE, 1).dyeColor(DyeColor.RED).build(),
                new ItemStackBuilder(Material.STAINED_GLASS_PANE, 1).dyeColor(DyeColor.GREEN).build()));
/*        super.addComponent(new GuiPanel(Arrays.asList(15, 16, 24, 25, 33, 34), new ItemStackBuilder(Material.STAINED_GLASS_PANE, 1).dyeColor(DyeColor.BLUE).build(), (event) -> {
            event.getWhoClicked().sendMessage("hogehoge");
        }));*/
        super.addComponent(new GuiItem(4, new ItemStackBuilder(Material.MINECART, 1).name(ChatColor.GREEN + "レッカー移動").lore(Collections.singletonList("アイテム化して持ち運べるようにします")).build(), (event) -> {
            car.tow();
            Bukkit.getScheduler().runTaskLater(MoCar.getInstance(), () -> {
                this.close();
                windows.stream().filter(window -> window.getInventory().equals(this.getInventory())).findFirst().ifPresent(windows::remove);
            }, 1L);
        }));
        super.open();
    }
}
