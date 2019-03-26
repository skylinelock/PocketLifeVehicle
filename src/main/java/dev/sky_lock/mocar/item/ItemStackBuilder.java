package dev.sky_lock.mocar.item;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * @author sky_lock
 */

public class ItemStackBuilder {
    private final ItemStack itemStack;

    public ItemStackBuilder(ItemStack itemStack) {
        this.itemStack = itemStack.clone();
        if (itemStack.hasItemMeta()) {
            return;
        }
        itemStack.setItemMeta(Bukkit.getServer().getItemFactory().getItemMeta(itemStack.getType()));
    }

    public static ItemStackBuilder of(Material type, int amount) {
        return new ItemStackBuilder(new ItemStack(type, amount));
    }

    public ItemStackBuilder durability(short durability) {
        itemStack.setDurability(durability);
        return this;
    }

    public ItemStackBuilder lore(List<String> lores) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(lores);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder name(String name) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder enchant(Enchantment enchantment, int level) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addEnchant(enchantment, level, true);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStackBuilder growing() {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addEnchant(new Glowing(), 1, true);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStack build() {
        return itemStack;
    }
}
