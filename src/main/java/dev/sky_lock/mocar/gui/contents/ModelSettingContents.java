package dev.sky_lock.mocar.gui.contents;

import dev.sky_lock.glassy.gui.MenuContents;
import dev.sky_lock.glassy.gui.Slot;
import dev.sky_lock.mocar.car.CarItem;
import dev.sky_lock.mocar.car.MaxSpeed;
import dev.sky_lock.mocar.gui.*;
import dev.sky_lock.mocar.item.ItemStackBuilder;
import dev.sky_lock.mocar.util.ListUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
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

            addSlot(new Slot(4, ItemStackBuilder.of(Material.ENDER_EYE, 1).name(ChatColor.RED + "閉じる").build(), event -> {
                player.closeInventory();
                EditSessions.destroy(player.getUniqueId());
            }));

            ItemStack nameItem = ItemStackBuilder.of(Material.NAME_TAG, 1).name("Name").build();
            if (session.getName() != null) {
                nameItem = new ItemStackBuilder(nameItem).lore(ListUtil.singleton(session.getName())).growing().build();
            }

            addSlot(new Slot(21, nameItem, event -> {
                StringEditor.open(player, StringEditor.Type.NAME);
            }));

            ItemStack speedItem = ItemStackBuilder.of(Material.DIAMOND, 1).name("MaxSpeed").build();
            MaxSpeed maxSpeed = session.getMaxSpeed();
            if (maxSpeed != null) {
                speedItem = new ItemStackBuilder(speedItem).lore(ListUtil.singleton(maxSpeed.getLabel())).growing().build();
            }

            addSlot(new Slot(23, speedItem, event -> {
                setPage(player, SettingIndex.SPEED.value());
            }));

            ItemStack idItem = ItemStackBuilder.of(Material.EMERALD, 1).name("Id").build();
            if (session.getId() != null) {
                idItem = new ItemStackBuilder(idItem).lore(ListUtil.singleton(session.getId())).growing().build();
            }
            addSlot(new Slot(19, idItem, event -> {
                StringEditor.open(player, StringEditor.Type.ID);
            }));

            ItemStack loreItem = ItemStackBuilder.of(Material.SIGN, 1).name("Lore").build();
            if (session.getLores() != null) {
                loreItem = new ItemStackBuilder(loreItem).lore(session.getLores()).growing().build();
            }

            addSlot(new Slot(28, loreItem, event -> {
                new LoreEditor(player).open();
            }));

            ItemStack carItem = ItemStackBuilder.of(Material.DIAMOND_HOE, 1).name("Item").build();
            if (session.getCarItem() != null) {
                CarItem item = session.getCarItem();
                List<String> details = Arrays.asList(item.getStack("").getType().toString(), String.valueOf(item.getStack("").getDurability()));
                carItem = new ItemStackBuilder(carItem).lore(details).growing().build();
            }
            addSlot(new Slot(30, carItem, event -> {
                setPage(player, SettingIndex.CAR_ITEM.value());

            }));

            ItemStack fuelItem = ItemStackBuilder.of(Material.COAL_BLOCK, 1).name("Fuel").build();
            if (session.getFuel() != 0.0F) {
                fuelItem = new ItemStackBuilder(fuelItem).lore(ListUtil.singleton(String.valueOf(session.getFuel()))).growing().build();
            }

            addSlot(new Slot(32, fuelItem, event -> {
                setPage(player, SettingIndex.FUEL.value());
            }));

            ItemStack capacityItem = ItemStackBuilder.of(Material.CHEST_MINECART, 1).name("Capacity").build();
            if (session.getCapacity() != -1) {
                capacityItem = new ItemStackBuilder(capacityItem).lore(ListUtil.singleton(String.valueOf(session.getCapacity()))).growing().build();
            }

            addSlot(new Slot(25, capacityItem, event -> {
                setPage(player, SettingIndex.CAPACITY.value());
            }));

            addCreateModelSlot(player, session);
        });
    }

    private void addCreateModelSlot(Player player, EditModelData session) {
        ItemStack createItem = ItemStackBuilder.of(Material.END_CRYSTAL, 1).name(ChatColor.GREEN + "追加する").build();
        addSlot(new Slot(49, createItem, event -> {
            setPage(player, SettingIndex.CONFIRM.value());
        }));

    }
}
