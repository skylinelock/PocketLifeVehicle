package dev.sky_lock.mocar.item;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Colorable;

import java.util.List;
import java.util.UUID;

/**
 * @author sky_lock
 */

public class ItemStackBuilder {

    private InventoryItem inventoryItem;

    public ItemStackBuilder(Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    public ItemStackBuilder(ItemStack itemStack) {
        Material type = itemStack.getType();
        int amount = itemStack.getAmount();
        switch (type) {
            case SKULL_ITEM:
                this.inventoryItem = new PlayerSkull(amount);
                return;
            case STAINED_GLASS_PANE:
                this.inventoryItem = new StainedGlassPane(amount);
                return;
            case WOOL:
                this.inventoryItem = new Wool(amount);
                return;
        }
        this.inventoryItem = new InventoryItem(itemStack);
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
