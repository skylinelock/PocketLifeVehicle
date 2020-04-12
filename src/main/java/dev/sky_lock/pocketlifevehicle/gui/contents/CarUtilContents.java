package dev.sky_lock.pocketlifevehicle.gui.contents;

import com.google.common.collect.ImmutableList;
import dev.sky_lock.menu.InventoryMenu;
import dev.sky_lock.menu.MenuContents;
import dev.sky_lock.menu.Slot;
import dev.sky_lock.menu.ToggleSlot;
import dev.sky_lock.pocketlifevehicle.vehicle.Car;
import dev.sky_lock.pocketlifevehicle.vehicle.CarEntities;
import dev.sky_lock.pocketlifevehicle.gui.CarUtilMenu;
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder;
import dev.sky_lock.pocketlifevehicle.item.PlayerSkull;
import dev.sky_lock.pocketlifevehicle.util.Profiles;
import dev.sky_lock.pocketlifevehicle.util.Formats;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sky_lock
 */

public class CarUtilContents extends MenuContents {
    private final Car car;
    private ItemStack refuelHopper;

    public CarUtilContents(Car car) {
        this.car = car;
        ItemStack closeItem = ItemStackBuilder.of(Material.ENDER_PEARL, 1).name(ChatColor.RED + "閉じる").build();
        ItemStack towItem = ItemStackBuilder.of(Material.MINECART, 1).name(colorizeTitle("レッカー移動")).lore(colorizeInfoAsList("アイテム化して持ち運べるようにします")).build();

        Slot ownerSlot = CarEntities.getOwner(car).map(owner -> {
            ItemStack playerSkull = PlayerSkull.of(owner, 1).toItemStack();
            ItemStack ownerSkull = ItemStackBuilder.of(playerSkull).name(colorizeTitle("所有者")).lore(colorizeContentAsLIst(Profiles.getName(owner))).build();
            return new Slot(20, ownerSkull, event -> {
            });
        }).orElse(null);

        ItemStack carInfoBook = ItemStackBuilder.of(Material.BOOK, 1).name(colorizeTitle("車両情報")).lore(carInfoLore()).build();

        Slot closeSlot = new Slot(4, closeItem, event -> {
            CarUtilMenu menu = (CarUtilMenu) event.getInventory().getHolder();
            menu.close((Player) event.getWhoClicked());
        });

        Slot towSlot = new Slot(11, towItem, event -> {
            CarEntities.tow(car);
            car.closeMenu((Player) event.getWhoClicked());
        });

        ItemStack wield = ItemStackBuilder.of(Material.LIME_DYE, 1).name(ChatColor.RED + "" + ChatColor.BOLD + "ハンドリングのアニメーションを無効にする").build();
        ItemStack notWield = ItemStackBuilder.of(Material.MAGENTA_DYE, 1).name(ChatColor.GREEN + "" + ChatColor.BOLD + "ハンドリングのアニメーションを有効にする").build();

        Slot wieldHandSlot = new ToggleSlot(13, car.getStatus().isWieldHand(), wield, notWield, (event) -> {
            car.getStatus().setWieldHand(false);
        }, (event) -> {
            car.getStatus().setWieldHand(true);
        });

        Slot carInfoSlot = new Slot(24, carInfoBook, event -> {
        });

        ItemStack keyClose = ItemStackBuilder.of(Material.BARRIER, 1).name(ChatColor.RED + "" + ChatColor.BOLD + "鍵を閉める").build();
        ItemStack keyOpen = ItemStackBuilder.of(Material.STRUCTURE_VOID, 1).name(ChatColor.AQUA + "" + ChatColor.BOLD + "鍵を開ける").build();

        Slot keySlot = new ToggleSlot(15, car.getStatus().isLocked(), keyOpen, keyClose, event -> {
            car.getStatus().setLocked(false);
            Player player = (Player) event.getWhoClicked();
            player.playSound(player.getLocation(), Sound.BLOCK_IRON_DOOR_OPEN, 1.0F, 1.4F);
        }, event -> {
            car.getStatus().setLocked(true);
            Player player = (Player) event.getWhoClicked();
            player.playSound(player.getLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, 1.0F, 1.4F);
        });

        refuelHopper = ItemStackBuilder.of(Material.HOPPER, 1).name(colorizeTitle("給油口")).lore(refuelInfo(car.getStatus().getFuel())).build();

        Slot fuelSlot = new Slot(22, refuelHopper, (event) -> {
            ItemStack cursor = event.getCursor();
            if (cursor == null) {
                return;
            }
            if (cursor.getType() != Material.COAL_BLOCK) {
                return;
            }
            boolean success = car.refuel(30F);
            if (success) {
                cursor.setAmount(cursor.getAmount() - 1);
                Player player = (Player) event.getWhoClicked();
                player.playSound(player.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1.0F, 0.6F);
                updateItemStack(22, ItemStackBuilder.of(refuelHopper).lore(refuelInfo(car.getStatus().getFuel())).build());
                InventoryMenu.of(player).ifPresent(menu -> {
                    menu.update();
                    setFuelGage(menu);
                });
            }
        });

        super.addSlot(ownerSlot, closeSlot, wieldHandSlot, towSlot, carInfoSlot, keySlot, fuelSlot);
    }

    @Override
    public void onFlip(InventoryMenu menu) {
        refuelHopper = ItemStackBuilder.of(refuelHopper).lore(refuelInfo(car.getStatus().getFuel())).build();
        updateItemStack(22, refuelHopper);
        menu.update();
        setFuelGage(menu);
    }

    private void setFuelGage(InventoryMenu menu) {
        ItemStack filled = ItemStackBuilder.of(Material.GREEN_STAINED_GLASS_PANE, 1).name(ChatColor.GREEN + "補充済み").build();
        ItemStack unFilled = ItemStackBuilder.of(Material.RED_STAINED_GLASS_PANE, 1).name(ChatColor.RED + "未補充").build();
        float fuel = car.getStatus().getFuel();
        float maxFuel = car.getModel().getSpec().getMaxFuel();
        float rate = fuel / maxFuel;
        int filledSlots = Math.round(9 * rate);
        for (int j = 0; j < 9; j++) {
            if (j < filledSlots) {
                menu.getInventory().setItem(27 + j, filled);
                menu.getInventory().setItem(36 + j, filled);
                menu.getInventory().setItem(45 + j, filled);
            } else {
                menu.getInventory().setItem(27 + j, unFilled);
                menu.getInventory().setItem(36 + j, unFilled);
                menu.getInventory().setItem(45 + j, unFilled);
            }
        }
    }

    private List<String> refuelInfo(float fuel) {
        return ImmutableList.of(ChatColor.GRAY + "残燃料 : " + Formats.truncateToOneDecimalPlace(Math.abs(fuel)), ChatColor.GRAY + "石炭ブロックを持って右クリック", ChatColor.GRAY + "すると燃料を補充できます");
    }

    private String colorizeTitle(String title) {
        return ChatColor.GOLD + "" + ChatColor.BOLD + title;
    }

    private List<String> colorizeInfoAsList(String... lore) {
        return Arrays.stream(lore).map(l -> ChatColor.GRAY + l).collect(Collectors.toList());
    }

    private List<String> colorizeContentAsLIst(String... lore) {
        return Arrays.stream(lore).map(l -> ChatColor.AQUA + l).collect(Collectors.toList());

    }

    private List<String> carInfoLore() {
        List<String> carInfo = new ArrayList<>();
        carInfo.add(ChatColor.GREEN + "名前     : " + ChatColor.RESET + car.getModel().getName());
        carInfo.add(ChatColor.GREEN + "最大燃料 : " + ChatColor.RESET + car.getModel().getSpec().getMaxFuel());
        carInfo.add(ChatColor.GREEN + "最高速度 : " + ChatColor.RESET + car.getModel().getSpec().getMaxSpeed());
        carInfo.add(ChatColor.GREEN + "説明 :");
        if (car.getModel().getLore() == null) {
            carInfo.add("- " + ChatColor.RESET + "なし");
            return carInfo;
        }
        car.getModel().getLore().forEach(lore -> carInfo.add("- " + ChatColor.RESET + lore));
        return carInfo;
    }
}