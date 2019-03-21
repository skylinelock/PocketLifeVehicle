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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        super.addComponent(new Button(11, new ItemStackBuilder(Material.MINECART, 1).name(ChatColor.GOLD + "" + ChatColor.BOLD + "レッカー移動").lore(ListUtil.singleton(ChatColor.GRAY + "アイテム化して持ち運べるようにします")).build(), (event) -> {
            CarEntities.tow(CarEntities.getOwner(car));
            close(player);
            player.closeInventory();
        }));

        setLockComponent(player, car);

        UUID owner = CarEntities.getOwner(car);
        super.addComponent(new Icon(20, getOwnerInfoItem(owner)));
        ItemStack hopper = new ItemStackBuilder(Material.HOPPER, 1).name(ChatColor.GOLD + "" + ChatColor.BOLD + "給油口").lore(Arrays.asList(ChatColor.GRAY + "残燃料 : " + car.getStatus().getFuel(), ChatColor.GRAY + "石炭ブロックを持って右クリックすると燃料を補充できます")).build();

        super.addComponent(new Button(22, hopper, (event) -> {
            ItemStack itemStack = event.getCursor();
            if (itemStack == null) {
                return;
            }
            if (itemStack.getType() != Material.COAL_BLOCK) {
                return;
            }
            boolean success = car.refuel(30F);
            if (success) {
                List<String> lore = Arrays.asList(ChatColor.GRAY + "残燃料 : " + car.getStatus().getFuel(), ChatColor.GRAY + "石炭ブロックを持って右クリックすると燃料を補充できます");
                getInventory().setItem(22, new ItemStackBuilder(hopper).lore(lore).build());
                itemStack.setAmount(itemStack.getAmount() - 1);
            }
        }));

        setCarInfoBook(car);

        super.addComponent(new Gage(27, 53, new ItemStackBuilder(Material.STAINED_GLASS_PANE, 1).dyeColor(DyeColor.RED).build(),
                new ItemStackBuilder(Material.STAINED_GLASS_PANE, 1).dyeColor(DyeColor.GREEN).build()));
    }

    private ItemStack getOwnerInfoItem(UUID owner) {
        return new ItemStackBuilder(Material.SKULL_ITEM, 1).skullOwner(owner).name(ChatColor.GOLD + "" + ChatColor.BOLD + "所有者").lore(ListUtil.singleton(ChatColor.AQUA + Bukkit.getOfflinePlayer(owner).getName())).build();
    }

    private void setLockComponent(Player player, CarArmorStand car) {
        ItemStack close = new ItemStackBuilder(Material.BARRIER, 1).name(ChatColor.RED + "" + ChatColor.BOLD + "鍵を閉める").build();
        ItemStack open = new ItemStackBuilder(Material.STRUCTURE_VOID, 1).name(ChatColor.AQUA + "" + ChatColor.BOLD + "鍵を開ける").build();
        boolean locked = car.getStatus().isLocked();
        super.addComponent(new ToggleButton(15, locked, open, close, (event) -> car.getStatus().setLocked(false), (event) -> car.getStatus().setLocked(true)));
    }

    private void setCarInfoBook(CarArmorStand car) {
        List<String> carInfo = new ArrayList<>();
        carInfo.add(ChatColor.GREEN + "識別子   : " + ChatColor.RESET + car.getModel().getId());
        carInfo.add(ChatColor.GREEN + "名前     : " + ChatColor.RESET + car.getModel().getName());
        carInfo.add(ChatColor.GREEN + "最大燃料 : " + ChatColor.RESET + car.getModel().getMaxFuel());
        carInfo.add(ChatColor.GREEN + "最高速度 : " + ChatColor.RESET + car.getModel().getMaxSpeed());
        carInfo.add(ChatColor.GREEN + "説明 :");
        car.getModel().getLores().forEach(lore -> {
            carInfo.add("- " + ChatColor.RESET + lore);
        });
        super.addComponent(new Icon(24, new ItemStackBuilder(Material.BOOK, 1).name(ChatColor.GOLD + "" + ChatColor.BOLD + "車両情報").lore(carInfo).build()));
    }

}
