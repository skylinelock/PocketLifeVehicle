package dev.sky_lock.mocar.util;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * @author sky_lock
 */

public class ItemStackBuilder {
    private Material material;
    private String name;
    private int amount;
    private double damage;
    private byte magicValue;
    private List<String> lores;

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

    public ItemStackBuilder lore(List<String> lores) {
        this.lores = lores;
        return this;
    }

    public ItemStackBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(material, amount, magicValue);
        ItemMeta meta = itemStack.getItemMeta();
        if (meta instanceof Damageable) {
            Damageable damageable = (Damageable) meta;
            damageable.damage(damage);
        }
        if (lores != null) {
            meta.setLore(lores);
        }
        if (name != null) {
            meta.setDisplayName(name);
        }
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
