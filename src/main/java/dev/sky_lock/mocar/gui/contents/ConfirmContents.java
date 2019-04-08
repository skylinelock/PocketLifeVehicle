package dev.sky_lock.mocar.gui.contents;

import dev.sky_lock.glassy.gui.InventoryMenu;
import dev.sky_lock.glassy.gui.MenuContents;
import dev.sky_lock.glassy.gui.Slot;
import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.CarModel;
import dev.sky_lock.mocar.car.ModelList;
import dev.sky_lock.mocar.gui.EditSessions;
import dev.sky_lock.mocar.gui.ModelMenuIndex;
import dev.sky_lock.mocar.item.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author sky_lock
 */

public class ConfirmContents extends MenuContents {

    public ConfirmContents(Player player) {
        Wool yes = new Wool();
        yes.setColor(DyeColor.GREEN);
        ItemStack yesItem = new ItemStackBuilder(yes.toItemStack(1)).name(ChatColor.GREEN + "Yes").build();
        Wool no = new Wool();
        no.setColor(DyeColor.RED);
        ItemStack noItem = new ItemStackBuilder(no.toItemStack(1)).name(ChatColor.RED + "No").build();

        addSlot(new Slot(20, yesItem, event -> {
            ItemStack clicked = event.getCurrentItem();
            EditSessions.of(event.getWhoClicked().getUniqueId()).ifPresent(session -> {
                if (session.getId() == null || session.getName() == null || session.getMaxSpeed() == null || session.getFuel() == 0.0F || session.getCarItem() == null || session.getCapacity() == null) {
                    List<String> lores = new ArrayList<>();
                    lores.add(ChatColor.RED + "設定が完了していません");
                    lores.add(ChatColor.RED + "未設定項目");
                    if (session.getId() == null) {
                        lores.add(ChatColor.RED + "- ID");
                    }
                    if (session.getName() == null) {
                        lores.add(ChatColor.RED + "- Name");
                    }
                    if (session.getMaxSpeed() == null) {
                        lores.add(ChatColor.RED + "- MaxSpeed");
                    }
                    if (session.getCarItem() == null) {
                        lores.add(ChatColor.RED + "- Item");
                    }
                    if (session.getFuel() == 0.0F) {
                        lores.add(ChatColor.RED + "- Fuel");
                    }
                    if (session.getCapacity() == null) {
                        lores.add(ChatColor.RED + "- Capacity");
                    }
                    ItemMeta itemMeta = clicked.getItemMeta();
                    itemMeta.setLore(lores);
                    clicked.setItemMeta(itemMeta);
                    event.setCurrentItem(clicked);
                    return;
                }
                if (ModelList.exists(session.getId())) {
                    ItemMeta itemMeta = clicked.getItemMeta();
                    itemMeta.setLore(Collections.singletonList(ChatColor.RED + "既に存在するIDです"));
                    clicked.setItemMeta(itemMeta);
                    event.setCurrentItem(clicked);
                    return;
                }
                ModelList.add(new CarModel(session.getId(), session.getCarItem(), session.getName(), session.getLores(), session.getFuel(), session.getMaxSpeed(), session.getCapacity()));
                player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "新しい車種を追加しました");
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
