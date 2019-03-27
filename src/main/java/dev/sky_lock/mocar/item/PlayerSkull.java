package dev.sky_lock.mocar.item;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

/**
 * @author sky_lock
 */

public class PlayerSkull {
    private final int amount;
    private OfflinePlayer player;

    private PlayerSkull(OfflinePlayer player, int amount) {
        this.player = player;
        this.amount = amount;
    }

    public static PlayerSkull of(UUID owner, int amount) {
        return new PlayerSkull(Bukkit.getOfflinePlayer(owner), amount);
    }

    public ItemStack toItemStack() {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, amount);
        if (player == null) {
            return itemStack;
        }
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setOwningPlayer(player);
        itemStack.setItemMeta(meta);
        return new ItemStackBuilder(itemStack).name(player.getName()).build();
    }
}
