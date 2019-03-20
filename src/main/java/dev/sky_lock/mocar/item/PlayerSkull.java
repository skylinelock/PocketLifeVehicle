package dev.sky_lock.mocar.item;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

/**
 * @author sky_lock
 */

public class PlayerSkull extends InventoryItem {
    private UUID owner;

    public PlayerSkull(int amount) {
        super(new ItemStack(Material.SKULL_ITEM, amount, (short) SkullType.PLAYER.ordinal()));
    }

    public void setOwner(UUID uuid) {
        this.owner = uuid;
    }

    public UUID getOwner() {
        return owner;
    }

    @Override
    public ItemStack toItemStack() {
        if (itemMeta == null) {
            return super.toItemStack();
        }
        if (owner != null) {
            SkullMeta skullMeta = (SkullMeta) itemMeta;
            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(owner));
            itemStack.setItemMeta(skullMeta);
        }
        return super.toItemStack();
    }
}
