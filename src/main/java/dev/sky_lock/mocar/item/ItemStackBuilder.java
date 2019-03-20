package dev.sky_lock.mocar.item;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Colorable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author sky_lock
 */

public class ItemStackBuilder {

    private InventoryItem inventoryItem;
    private Material material;
    private int amount;
    private short damage;
    private DyeColor color;
    private Map<Enchantment, Integer> enchantMap = new HashMap<>();

    public ItemStackBuilder(Material material, int amount) {
        switch (material) {
            case SKULL_ITEM:
                this.inventoryItem = new PlayerSkull(amount);
                return;
            case STAINED_GLASS_PANE:
                this.inventoryItem = new StainedGlassPane(amount);
                return;
        }
        this.inventoryItem = new InventoryItem(new ItemStack(material, amount));
    }

    public ItemStackBuilder skullOwner(UUID uuid) {
        if (inventoryItem instanceof PlayerSkull) {
            ((PlayerSkull) inventoryItem).setOwner(uuid);
        }
        return this;
    }

    public ItemStackBuilder durability(short damage) {
        this.inventoryItem.setDurability(damage);
        return this;
    }

    public ItemStackBuilder dyeColor(DyeColor color) {
        if (inventoryItem instanceof Colorable) {
            ((Colorable) inventoryItem).setColor(color);
        }
        return this;
    }

    public ItemStackBuilder lore(List<String> lores) {
        this.inventoryItem.setLore(lores);
        return this;
    }

    public ItemStackBuilder name(String name) {
        this.inventoryItem.setDisplayName(name);
        return this;
    }

    public ItemStackBuilder enchant(Enchantment enchantment, int level) {
        this.inventoryItem.addEnchant(enchantment, level);
        return this;
    }

    public ItemStack build() {
        return inventoryItem.toItemStack();
    }
}
