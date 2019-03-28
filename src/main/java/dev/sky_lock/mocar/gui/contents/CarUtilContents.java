package dev.sky_lock.mocar.gui.contents;

import dev.sky_lock.glassy.gui.MenuContents;
import dev.sky_lock.glassy.gui.Slot;
import dev.sky_lock.glassy.gui.ToggleSlot;
import dev.sky_lock.mocar.car.CarArmorStand;
import dev.sky_lock.mocar.car.CarEntities;
import dev.sky_lock.mocar.gui.CarUtilMenu;
import dev.sky_lock.mocar.item.ItemStackBuilder;
import dev.sky_lock.mocar.item.PlayerSkull;
import dev.sky_lock.mocar.util.PlayerInfo;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
    private final CarArmorStand car;
    private final Slot closeSlot;
    private final Slot towSlot;
    private final Slot ownerSlot;
    private final Slot carInfoSlot;

    public CarUtilContents(CarArmorStand car) {
        this.car = car;
        ItemStack closeItem = ItemStackBuilder.of(Material.ENDER_PEARL, 1).name(ChatColor.RED + "閉じる").build();
        ItemStack towItem = ItemStackBuilder.of(Material.MINECART, 1).name(colorizeTitle("レッカー移動")).lore(colorizeInfoAsList("アイテム化して持ち運べるようにします")).build();

        this.ownerSlot = CarEntities.getOwner(car).map(owner -> {
            ItemStack playerSkull = PlayerSkull.of(owner, 1).toItemStack();
            ItemStack ownerSkull = new ItemStackBuilder(playerSkull).name(colorizeTitle("所有者")).lore(colorizeContentAsLIst(PlayerInfo.getName(owner))).build();
            return new Slot(20, ownerSkull, event -> {
            });
        }).orElse(null);

        ItemStack carInfoBook = ItemStackBuilder.of(Material.BOOK, 1).name(colorizeTitle("車輌情報")).lore(carInfoLore()).build();

        this.closeSlot = new Slot(4, closeItem, event -> {
            CarUtilMenu menu = (CarUtilMenu) event.getInventory().getHolder();
            menu.close((Player) event.getWhoClicked());
        });
        this.towSlot = new Slot(11, towItem, event -> {
            Player player = (Player) event.getWhoClicked();
            CarEntities.tow(player.getUniqueId());
            ((CarUtilMenu) event.getInventory().getHolder()).close(player);
            player.closeInventory();
        });
        this.carInfoSlot = new Slot(24, carInfoBook, event -> {
        });
    }

    @Override
    public void open(Player player) {
        super.addSlot(closeSlot);
        super.addSlot(towSlot);
        CarEntities.getOwner(car).ifPresent(owner -> {
            super.addSlot(ownerSlot);
        });
        ItemStack keyClose = ItemStackBuilder.of(Material.BARRIER, 1).name(ChatColor.RED + "" + ChatColor.BOLD + "鍵を閉める").build();
        ItemStack keyOpen = ItemStackBuilder.of(Material.STRUCTURE_VOID, 1).name(ChatColor.AQUA + "" + ChatColor.BOLD + "鍵を開ける").build();

        super.addSlot(carInfoSlot);

        super.addSlot(new ToggleSlot(15, car.getStatus().isLocked(), keyOpen, keyClose, event -> {
            car.getStatus().setLocked(false);
        }, event -> {
            car.getStatus().setLocked(true);
        }));

        ItemStack refuelHopper = ItemStackBuilder.of(Material.HOPPER, 1).name(colorizeTitle("給油口")).lore(refuelInfo(car.getStatus().getFuel())).build();

        super.addSlot(new Slot(22, refuelHopper, (event) -> {
            ItemStack cursor = event.getCursor();
            if (cursor == null) {
                return;
            }
            if (cursor.getType() != Material.COAL_BLOCK) {
                return;
            }
            boolean success = car.refuel(30F);
            if (success) {
                event.getInventory().setItem(22, new ItemStackBuilder(refuelHopper).lore(refuelInfo(car.getStatus().getFuel())).build());
                cursor.setAmount(cursor.getAmount() - 1);
            }
        }));
    }

    private List<String> refuelInfo(float fuel) {
        return Arrays.asList(ChatColor.GRAY + "残燃料 : " + String.format("%.1f", Math.abs(fuel)), ChatColor.GRAY + "石炭ブロックを持って右クリック", ChatColor.GRAY + "すると燃料を補充できます");

    }

    private String colorizeTitle(String title) {
        return ChatColor.GOLD + "" + ChatColor.BOLD + title;
    }

    private List<String> colorizeInfoAsList(String... lores) {
        return Arrays.stream(lores).map(lore -> ChatColor.GRAY + lore).collect(Collectors.toList());
    }

    private List<String> colorizeContentAsLIst(String... lores) {
        return Arrays.stream(lores).map(lore -> ChatColor.AQUA + lore).collect(Collectors.toList());

    }

    private List<String> carInfoLore() {
        List<String> carInfo = new ArrayList<>();
        carInfo.add(ChatColor.GREEN + "識別子   : " + ChatColor.RESET + car.getModel().getId());
        carInfo.add(ChatColor.GREEN + "名前     : " + ChatColor.RESET + car.getModel().getName());
        carInfo.add(ChatColor.GREEN + "最大燃料 : " + ChatColor.RESET + car.getModel().getMaxFuel());
        carInfo.add(ChatColor.GREEN + "最高速度 : " + ChatColor.RESET + car.getModel().getMaxSpeed());
        carInfo.add(ChatColor.GREEN + "説明 :");
        car.getModel().getLores().forEach(lore -> carInfo.add("- " + ChatColor.RESET + lore));
        return carInfo;
    }
}
