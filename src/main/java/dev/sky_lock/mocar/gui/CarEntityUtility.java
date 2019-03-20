package dev.sky_lock.mocar.gui;

import dev.sky_lock.mocar.car.CarArmorStand;
import dev.sky_lock.mocar.car.CarEntities;
import dev.sky_lock.mocar.gui.api.*;
import dev.sky_lock.mocar.item.ItemStackBuilder;
import dev.sky_lock.mocar.util.ListUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.UUID;

/**
 * @author sky_lock
 */

public class CarEntityUtility extends GuiWindow {

    public CarEntityUtility(Player player, CarArmorStand car) {
        super("CarUtility", player, GuiType.BIG);
        super.addComponent(new Button(4, new ItemStackBuilder(Material.ENDER_PEARL, 1).name(ChatColor.RED + "閉じる").build(), (event) -> {
            player.closeInventory();
        }));
        super.addComponent(new Button(11, new ItemStackBuilder(Material.MINECART, 1).name(ChatColor.AQUA + "レッカー移動").lore(Collections.singletonList(ChatColor.GRAY + "アイテム化して持ち運べるようにします")).build(), (event) -> {
            CarEntities.tow(player.getUniqueId());
            close(player);
            player.closeInventory();
        }));
        UUID owner = CarEntities.getOwner(car);
        super.addComponent(new Icon(20, getOwnerInfoItem(owner)));

        super.addComponent(new Gage(36, 53, new ItemStackBuilder(Material.STAINED_GLASS_PANE, 1).dyeColor(DyeColor.RED).build(),
                new ItemStackBuilder(Material.STAINED_GLASS_PANE, 1).dyeColor(DyeColor.GREEN).build()));
    }

    private ItemStack getOwnerInfoItem(UUID owner) {
        return new ItemStackBuilder(Material.SKULL_ITEM, 1).skullOwner(owner).name(ChatColor.AQUA + "所有者").lore(ListUtil.singleton(ChatColor.GOLD + Bukkit.getOfflinePlayer(owner).getName())).build();
    }
}
