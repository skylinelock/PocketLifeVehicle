package dev.sky_lock.mocar.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * @author sky_lock
 */

public class InventoryItem {
    protected final ItemStack itemStack;
    final ItemMeta itemMeta;
    String displayName;
    List<String> lores;

    public InventoryItem(ItemStack itemStack) {
        if (itemStack == null) {
            throw new IllegalArgumentException("ItemStack cannot be null");
        }
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemStack toItemStack() {
        if (itemMeta == null) {
            return itemStack;
        }
        if (displayName != null) {
            itemMeta.setDisplayName(displayName);
        }
        if (lores != null) {
            itemMeta.setLore(lores);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void setLore(List<String> lores) {
        this.lores = lores;
    }

    public List<String> getLore() {
        return lores;
    }

    public void addEnchant(Enchantment enchantment, int level) {
        if (itemMeta == null) {
            return;
        }
        itemMeta.addEnchant(enchantment, level, true);
    }

    public void setDurability(short durability) {
        itemStack.setDurability(durability);
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getType() {
        return itemStack.getType();
    }
}
