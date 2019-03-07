package dev.sky_lock.mocar.util;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author sky_lock
 */

public class ItemStackBuilder {
    private Material material;
    private int amount;
    private double damage;
    private byte magicValue;

    public ItemStackBuilder(Material material, int amount) {
        this.material = material;
        this.amount = amount;
    }

    public ItemStackBuilder damage(double damage) {
        this.damage = damage;
        return this;
    }

    public ItemStackBuilder dyeColor(DyeColor color) {
        this.magicValue = color.getWoolData();
        return this;
    }

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(material, amount, magicValue);
        ItemMeta meta = itemStack.getItemMeta();
        if (meta instanceof Damageable) {
            Damageable damageable = (Damageable) meta;
            damageable.damage(damage);
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }
}
