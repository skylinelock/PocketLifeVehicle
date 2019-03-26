package dev.sky_lock.mocar.gui.contents;

import dev.sky_lock.glassy.gui.MenuContents;
import dev.sky_lock.glassy.gui.Slot;
import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.CarItem;
import dev.sky_lock.mocar.car.CarModel;
import dev.sky_lock.mocar.car.ModelList;
import dev.sky_lock.mocar.car.Speed;
import dev.sky_lock.mocar.gui.*;
import dev.sky_lock.mocar.item.ItemStackBuilder;
import dev.sky_lock.mocar.util.ListUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author sky_lock
 */

public class ModelSettingContents extends MenuContents {

    @Override
    public void open(Player player) {
        EditSessions.get(player.getUniqueId()).ifPresent(session -> {

            addSlot(new Slot(3, ItemStackBuilder.of(Material.ENDER_PEARL, 1).name(ChatColor.RED + "戻る").build(), event -> {
                new EditCarModelMenu(player).open(player);
                EditSessions.destroy(player.getUniqueId());
            }));

            addSlot(new Slot(4, ItemStackBuilder.of(Material.EYE_OF_ENDER, 1).name(ChatColor.RED + "閉じる").build(), event -> {
                player.closeInventory();
                EditSessions.destroy(player.getUniqueId());
            }));

            ItemStack nameItem = ItemStackBuilder.of(Material.NAME_TAG, 1).name("Name").build();
            if (session.getName() != null) {
                nameItem = new ItemStackBuilder(nameItem).lore(ListUtil.singleton(session.getName())).growing().build();
            }

            addSlot(new Slot(22, nameItem, event -> {
                StringEditor.open(player, StringEditor.Type.NAME);
            }));

            ItemStack speedItem = ItemStackBuilder.of(Material.DIAMOND, 1).name("Speed").build();
            Speed speed = session.getSpeed();
            if (speed != null) {
                speedItem = new ItemStackBuilder(speedItem).lore(ListUtil.singleton(speed.getLabel())).growing().build();
            }

            addSlot(new Slot(24, speedItem, event -> {
                new SelectSpeedMenu(player).open(player);
            }));

            ItemStack idItem = ItemStackBuilder.of(Material.EMERALD, 1).name("Id").build();
            if (session.getId() != null) {
                idItem = new ItemStackBuilder(idItem).lore(ListUtil.singleton(session.getId())).growing().build();
            }
            addSlot(new Slot(20, idItem, event -> {
                StringEditor.open(player, StringEditor.Type.ID);
            }));

            ItemStack loreItem = ItemStackBuilder.of(Material.SIGN, 1).name("Lore").build();
            if (session.getLores() != null) {
                loreItem = new ItemStackBuilder(loreItem).lore(session.getLores()).growing().build();
            }

            addSlot(new Slot(29, loreItem, event -> {
                new SignEditor().open(player);
            }));

            ItemStack carItem = ItemStackBuilder.of(Material.DIAMOND_HOE, 1).name("Item").build();
            if (session.getCarItem() != null) {
                CarItem item = session.getCarItem();
                List<String> details = Arrays.asList(item.getStack("").getType().toString(), String.valueOf(item.getStack("").getDurability()));
                carItem = new ItemStackBuilder(carItem).lore(details).growing().build();
            }
            addSlot(new Slot(31, carItem, event -> {
                new SelectCarItemMenu(player).open(player);
            }));

            ItemStack fuelItem = ItemStackBuilder.of(Material.COAL_BLOCK, 1).name("Fuel").build();
            if (session.getFuel() != 0.0F) {
                fuelItem = new ItemStackBuilder(fuelItem).lore(ListUtil.singleton(String.valueOf(session.getFuel()))).growing().build();
            }

            addSlot(new Slot(33, fuelItem, event -> {
                new SelectFuelMenu(player).open(player);
            }));

            addCreateModelSlot(player, session);
        });
    }

    private void addDecorationSlot(int slotindex, ItemStack itemStack) {
        addSlot(new Slot(slotindex, itemStack, event -> {}));
    }

    private void addCreateModelSlot(Player player, EditModelData session) {
        ItemStack createItem = ItemStackBuilder.of(Material.END_CRYSTAL, 1).name(ChatColor.GREEN + "追加する").build();
        addSlot(new Slot(49, createItem, event -> {
            new ConfirmMenu(player, (event1) -> {
                ItemStack clicked = event1.getCurrentItem();
                if (session.getId() == null || session.getName() == null || session.getSpeed() == null || session.getFuel() == 0.0F || session.getCarItem() == null) {
                    List<String> lores = new ArrayList<>();
                    lores.add(ChatColor.RED + "設定が完了していません");
                    lores.add(ChatColor.RED + "未設定項目");
                    if (session.getId() == null) {
                        lores.add(ChatColor.RED + "- ID");
                    }
                    if (session.getName() == null) {
                        lores.add(ChatColor.RED + "- Name");
                    }
                    if (session.getSpeed() == null) {
                        lores.add(ChatColor.RED + "- Speed");
                    }
                    if (session.getCarItem() == null) {
                        lores.add(ChatColor.RED + "- Item");
                    }
                    if (session.getFuel() == 0.0F) {
                        lores.add(ChatColor.RED + "- Fuel");
                    }
                    ItemMeta itemMeta = clicked.getItemMeta();
                    itemMeta.setLore(lores);
                    clicked.setItemMeta(itemMeta);
                    event1.setCurrentItem(clicked);
                    return;
                }
                if (ModelList.exists(session.getId())) {
                    ItemMeta itemMeta = clicked.getItemMeta();
                    itemMeta.setLore(Collections.singletonList(ChatColor.RED + "既に存在するIDです"));
                    clicked.setItemMeta(itemMeta);
                    event1.setCurrentItem(clicked);
                    return;
                }
                ModelList.add(new CarModel(session.getId(), session.getCarItem(), session.getName(), session.getLores(), session.getFuel(), session.getSpeed().ordinal() + 1));
                player.sendMessage(MoCar.PREFIX + ChatColor.GREEN + "新しい車種を追加しました");
                EditSessions.destroy(player.getUniqueId());
                player.closeInventory();
            }).open(player);
        }));
    }
}
