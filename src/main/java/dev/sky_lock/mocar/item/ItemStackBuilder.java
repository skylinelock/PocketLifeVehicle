package dev.sky_lock.mocar.item;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Damageable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<Enchantment, Integer> enchantMap = new HashMap<>();

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

    public ItemStackBuilder enchant(Enchantment enchantment, int level) {
        this.enchantMap.put(enchantment, level);
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
        enchantMap.forEach((key, value) -> meta.addEnchant(key, value, true));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
