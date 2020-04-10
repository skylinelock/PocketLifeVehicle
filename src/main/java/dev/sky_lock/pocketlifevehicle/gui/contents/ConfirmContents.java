package dev.sky_lock.pocketlifevehicle.gui.contents;

import dev.sky_lock.menu.InventoryMenu;
import dev.sky_lock.menu.MenuContents;
import dev.sky_lock.menu.Slot;
import dev.sky_lock.pocketlifevehicle.PLVehicle;
import dev.sky_lock.pocketlifevehicle.car.*;
import dev.sky_lock.pocketlifevehicle.gui.EditSessions;
import dev.sky_lock.pocketlifevehicle.gui.ModelMenuIndex;
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author sky_lock
 */

public class ConfirmContents extends MenuContents {

    public ConfirmContents(Player player) {
        Wool yes = new Wool();
        yes.setColor(DyeColor.GREEN);
        ItemStack yesItem = ItemStackBuilder.of(yes.toItemStack(1)).name(ChatColor.GREEN + "追加する").build();
        Wool no = new Wool();
        no.setColor(DyeColor.RED);
        ItemStack noItem = ItemStackBuilder.of(no.toItemStack(1)).name(ChatColor.RED + "追加しない").build();

        addSlot(new Slot(20, yesItem, event -> {
            ItemStack clicked = Objects.requireNonNull(event.getCurrentItem());
            EditSessions.of(event.getWhoClicked().getUniqueId()).ifPresent(session -> {

                String id = session.getId();
                String name = session.getName();
                Capacity capacity = session.getCapacity();
                MaxSpeed maxSpeed = session.getMaxSpeed();
                float maxFuel = session.getFuel();
                CarItem carItem = session.getCarItem();
                float collideBaseSide = session.getCollideBaseSide();
                float collideHeight = session.getCollideHeight();
                float height = session.getHeight();

                if (id == null || name == null || maxSpeed == null || maxFuel == 0.0F || carItem == null || capacity == null || height == -1) {
                    List<String> lores = new ArrayList<>();
                    lores.add(ChatColor.RED + "設定が完了していません");
                    lores.add(ChatColor.RED + "未設定項目");
                    if (id == null) {
                        lores.add(ChatColor.RED + "- ID");
                    }
                    if (name == null) {
                        lores.add(ChatColor.RED + "- 名前");
                    }
                    if (maxSpeed == null) {
                        lores.add(ChatColor.RED + "- 最高速度");
                    }
                    if (carItem == null) {
                        lores.add(ChatColor.RED + "- アイテム");
                    }
                    if (maxFuel == 0.0F) {
                        lores.add(ChatColor.RED + "- 燃料");
                    }
                    if (capacity == null) {
                        lores.add(ChatColor.RED + "- 乗車人数");
                    }
                    if (height == -1) {
                        lores.add(ChatColor.RED + "- 座高");
                    }
                    ItemMeta itemMeta = Objects.requireNonNull(clicked.getItemMeta());
                    itemMeta.setLore(lores);
                    clicked.setItemMeta(itemMeta);
                    event.setCurrentItem(clicked);
                    return;
                }

                if (ModelList.exists(session.getId())) {
                    ItemMeta itemMeta = Objects.requireNonNull(clicked.getItemMeta());
                    itemMeta.setLore(Collections.singletonList(ChatColor.RED + "既に存在するIDです"));
                    clicked.setItemMeta(itemMeta);
                    event.setCurrentItem(clicked);
                    return;
                }
                List<String> carLores = session.getLores();
                CarModel model = CarModelBuilder.of(session.getId())
                        .name(name)
                        .capacity(capacity)
                        .height(height)
                        .collideBox(collideBaseSide, collideHeight)
                        .maxFuel(maxFuel)
                        .maxSpeed(maxSpeed)
                        .item(carItem)
                        .sound(CarSound.NONE)
                        .steering(SteeringLevel.NORMAL)
                        .lores(carLores)
                        .build();
                ModelList.add(model);
                player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "新しい車種を追加しました");
                EditSessions.destroy(player.getUniqueId());
                player.closeInventory();
            });
        }));
        addSlot(new Slot(24, noItem, event -> {
            InventoryMenu.of(player).ifPresent(menu -> menu.flip(player, ModelMenuIndex.SETTING.value()));
        }));


    }

    @Override
    public void onFlip(InventoryMenu inventoryMenu) {

    }
}
