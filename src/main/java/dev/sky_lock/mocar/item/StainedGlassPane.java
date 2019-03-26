package dev.sky_lock.mocar.item;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author sky_lock
 */

public class StainedGlassPane {

    private final DyeColor color;
    private final int amount;

    public StainedGlassPane(DyeColor color, int amount) {
        this.color = color;
        this.amount = amount;
    }

    public static StainedGlassPane of(DyeColor color, int amount) {
        return new StainedGlassPane(color, amount);
    }

    public ItemStack toItemStack() {
        return ItemStackBuilder.of(Material.STAINED_GLASS_PANE, amount).durability(color.getWoolData()).build();
    }
}
