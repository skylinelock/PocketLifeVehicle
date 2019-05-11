package dev.sky_lock.mocar.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

/**
 * @author sky_lock
 */

public class ItemStackBuilder {
    private final ItemStack itemStack;

    private ItemStackBuilder(ItemStack itemStack) {
        this.itemStack = itemStack.clone();
    }

    public static ItemStackBuilder of(Material type, int amount) {
        if (type == Material.AIR) {
            throw new IllegalArgumentException("Material cannot be Material.AIR");
        }
        return new ItemStackBuilder(new ItemStack(type, amount));
    }

    public static ItemStackBuilder of(ItemStack itemStack) {
        if (itemStack.getType() == Material.AIR) {
            throw new IllegalArgumentException("Material cannot be Material.AIR");
        }
        return new ItemStackBuilder(itemStack);
    }

    public ItemStackBuilder durability(short damage) {
        ItemMeta itemMeta = Objects.requireNonNull(itemStack.getItemMeta());
        ((Damageable) itemMeta).setDamage(damage);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder lore(List<String> lores) {
        ItemMeta itemMeta = Objects.requireNonNull(itemStack.getItemMeta());
        itemMeta.setLore(lores);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder name(String name) {
        ItemMeta itemMeta = Objects.requireNonNull(itemStack.getItemMeta());
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder unbreakable(boolean unbreakable) {
        ItemMeta itemMeta = Objects.requireNonNull(itemStack.getItemMeta());
        itemMeta.setUnbreakable(unbreakable);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder grow() {
        ItemMeta itemMeta = Objects.requireNonNull(itemStack.getItemMeta());
        itemMeta.addEnchant(new Glowing(), 1, true);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStack build() {
        return itemStack;
    }
}
