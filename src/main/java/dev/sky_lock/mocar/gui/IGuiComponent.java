package dev.sky_lock.mocar.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author sky_lock
 */

public interface IGuiComponent {

    void onClick(InventoryClickEvent event);

    SlotRange getSlotRange();

    ItemStack getItemStack();
}
