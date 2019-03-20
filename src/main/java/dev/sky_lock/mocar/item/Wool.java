package dev.sky_lock.mocar.item;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Colorable;

/**
 * @author sky_lock
 */

public class Wool extends InventoryItem implements Colorable {
    private DyeColor color;

    public Wool(int amount) {
        super(new ItemStack(Material.WOOL, amount));
    }

    @Override
    public void setColor(DyeColor dyeColor) {
        this.color = dyeColor;
    }

    @Override
    public DyeColor getColor() {
        return color;
    }

    @Override
    public ItemStack toItemStack() {
        if (itemMeta == null) {
            return super.toItemStack();
        }
        if (color == null) {
            return super.toItemStack();
        }
        itemStack.setDurability(color.getWoolData());
        return super.toItemStack();
    }
}
