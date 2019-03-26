package dev.sky_lock.mocar.item;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * @author sky_lock
 */

public class PlayerSkull {
    private final int amount;
    private OfflinePlayer player;

    public PlayerSkull(OfflinePlayer player, int amount) {
        this.player = player;
        this.amount = amount;
    }

    public static PlayerSkull of(UUID owner, int amount) {
        return new PlayerSkull(Bukkit.getOfflinePlayer(owner), amount);
    }

    public ItemStack toItemStack() {
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, amount, (short) SkullType.PLAYER.ordinal());
        if (player == null) {
            return itemStack;
        }
        return new ItemStackBuilder(itemStack).name(player.getName()).build();
    }
}
