package dev.sky_lock.pocketlifevehicle.item;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagType;

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

    public ItemStackBuilder customModelData(int id) {
        ItemMeta meta = Objects.requireNonNull(itemStack.getItemMeta());
        meta.setCustomModelData(id);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder lore(List<String> lores) {
        ItemMeta meta = Objects.requireNonNull(itemStack.getItemMeta());
        meta.setLore(lores);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder name(String name) {
        ItemMeta meta = Objects.requireNonNull(itemStack.getItemMeta());
        meta.setDisplayName(name);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder unbreakable(boolean unbreakable) {
        ItemMeta meta = Objects.requireNonNull(itemStack.getItemMeta());
        meta.setUnbreakable(unbreakable);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder itemFlags(ItemFlag... flags) {
        ItemMeta meta = Objects.requireNonNull(itemStack.getItemMeta());
        meta.addItemFlags(flags);
        itemStack.setItemMeta(meta);
        return this;
    }

    public <T, Z> ItemStackBuilder tag(NamespacedKey key, ItemTagType<T, Z> tagType, Z object) {
        ItemMeta meta = Objects.requireNonNull(itemStack.getItemMeta());
        CustomItemTagContainer tagContainer = meta.getCustomTagContainer();
        tagContainer.setCustomTag(key, tagType, object);
        itemStack.setItemMeta(meta);
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
