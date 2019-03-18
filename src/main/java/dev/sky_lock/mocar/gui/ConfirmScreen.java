package dev.sky_lock.mocar.gui;

import dev.sky_lock.mocar.gui.api.Button;
import dev.sky_lock.mocar.gui.api.GuiType;
import dev.sky_lock.mocar.gui.api.GuiWindow;
import dev.sky_lock.mocar.util.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.util.Consumer;

/**
 * @author sky_lock
 */

public class ConfirmScreen extends GuiWindow {

    public ConfirmScreen(Player holder, Consumer<InventoryClickEvent> todo) {
        super("Are you sure?", holder, GuiType.WIDE);
        super.addComponent(new Button(20, new ItemStackBuilder(Material.WOOL, 1).dyeColor(DyeColor.GREEN).name(ChatColor.GREEN + "YES").build(), todo::accept));
        super.addComponent(new Button(24, new ItemStackBuilder(Material.WOOL, 1).dyeColor(DyeColor.RED).name(ChatColor.RED + "NO").build(), (event) -> {
            new ModelSetting(holder).open(holder);
        }));
    }
}
