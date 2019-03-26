package dev.sky_lock.mocar.gui.contents;

import dev.sky_lock.glassy.gui.MenuContents;
import dev.sky_lock.glassy.gui.Slot;
import dev.sky_lock.mocar.gui.ModelSettingMenu;
import dev.sky_lock.mocar.item.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;
import org.bukkit.util.Consumer;

/**
 * @author sky_lock
 */

public class ConfirmContents extends MenuContents {
    private final Consumer<InventoryClickEvent> todo;

    public ConfirmContents(Consumer<InventoryClickEvent> event) {
        this.todo = event;
    }

    @Override
    public void open(Player player) {
        Wool yes = new Wool();
        yes.setColor(DyeColor.GREEN);
        ItemStack yesItem = new ItemStackBuilder(yes.toItemStack(1)).name(ChatColor.GREEN + "Yes").build();
        Wool no = new Wool();
        no.setColor(DyeColor.RED);
        ItemStack noItem = new ItemStackBuilder(no.toItemStack(1)).name(ChatColor.RED + "No").build();

        addSlot(new Slot(20, yesItem, todo));
        addSlot(new Slot(24, noItem, event -> {
            new ModelSettingMenu(player).open(player);
        }));
    }
}
