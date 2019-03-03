package dev.sky_lock.mocar.gui;

import org.bukkit.inventory.ItemStack;

/**
 * @author sky_lock
 */

public class GuiItem {
    private final ItemStack itemOnGui;
    private final int slot;

    public GuiItem(ItemStack itemOnGui, int slot) {
        this.itemOnGui = itemOnGui;
        this.slot = slot;
    }


}
