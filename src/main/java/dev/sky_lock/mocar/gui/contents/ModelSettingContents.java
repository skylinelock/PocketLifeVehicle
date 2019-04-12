package dev.sky_lock.mocar.gui.contents;

import dev.sky_lock.glassy.gui.InventoryMenu;
import dev.sky_lock.glassy.gui.MenuContents;
import dev.sky_lock.glassy.gui.Slot;
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
    private final Player player;
    private ItemStack carItem = ItemStackBuilder.of(Material.DIAMOND_HOE, 1).name("Item").build();
    private ItemStack capacityItem = ItemStackBuilder.of(Material.CHEST_MINECART, 1).name("Capacity").build();
    private ItemStack fuelItem = ItemStackBuilder.of(Material.COAL_BLOCK, 1).name("Fuel").build();
    private ItemStack idItem = ItemStackBuilder.of(Material.EMERALD, 1).name("Id").build();
    private ItemStack nameItem = ItemStackBuilder.of(Material.NAME_TAG, 1).name("Name").build();
    private ItemStack speedItem = ItemStackBuilder.of(Material.DIAMOND, 1).name("MaxSpeed").build();
    private ItemStack loreItem = ItemStackBuilder.of(Material.SIGN, 1).name("Lore").build();


    public ModelSettingContents(Player player) {
        this.player = player;
        EditSessions.of(player.getUniqueId()).ifPresent(session -> {
            addSlot(new Slot(3, ItemStackBuilder.of(Material.ENDER_PEARL, 1).name(ChatColor.RED + "戻る").build(), event -> {
                InventoryMenu.of(player).ifPresent(menu -> menu.flip(player, ModelMenuIndex.MAIN_MENU.value()));
                EditSessions.destroy(player.getUniqueId());
            }));

            addSlot(new Slot(4, ItemStackBuilder.of(Material.ENDER_EYE, 1).name(ChatColor.RED + "閉じる").build(), event -> {
                InventoryMenu.of(player).ifPresent(menu -> menu.close((Player) event.getWhoClicked()));
                EditSessions.destroy(player.getUniqueId());
            }));

            addSlot(new Slot(21, nameItem, event -> {
                InventoryMenu.of(player).ifPresent(menu -> {
                    StringEditor.open(player, StringEditor.Type.NAME, (ModelSettingMenu) menu);
                });
            }));

            addSlot(new Slot(23, speedItem, event -> {
                InventoryMenu.of(player).ifPresent(menu -> {
                    menu.flip(player, ModelMenuIndex.SPEED.value());
                });
            }));

            addSlot(new Slot(19, idItem, event -> {
                InventoryMenu.of(player).ifPresent(menu -> {
                    StringEditor.open(player, StringEditor.Type.ID, (ModelSettingMenu) menu);
                });
            }));

            addSlot(new Slot(28, loreItem, event -> {
                new LoreEditor(player).open();
            }));

            addSlot(new Slot(30, carItem, event -> {
                InventoryMenu.of(player).ifPresent(menu -> menu.flip(player, ModelMenuIndex.CAR_ITEM.value()));
            }));

            addSlot(new Slot(32, fuelItem, event -> {
                InventoryMenu.of(player).ifPresent(menu -> menu.flip(player, ModelMenuIndex.FUEL.value()));
            }));

            addSlot(new Slot(25, capacityItem, event -> {
                InventoryMenu.of(player).ifPresent(menu -> menu.flip(player, ModelMenuIndex.CAPACITY.value()));
            }));

            addCreateModelSlot(player, session);
        });
    }

    @Override
    public void onFlip(InventoryMenu menu) {
        EditSessions.of(player.getUniqueId()).ifPresent(session -> {
            if (session.getCarItem() != null) {
                int damage = session.getCarItem().getDamage();
                List<String> details = Arrays.asList(session.getCarItem().getType().toString(), String.valueOf(damage));
                carItem = new ItemStackBuilder(carItem).lore(details).grow().build();
                updateItemStack(30, carItem);
            }
            if (session.getCapacity() != null) {
                capacityItem = new ItemStackBuilder(capacityItem).lore(ListUtil.singleton(String.valueOf(session.getCapacity()))).grow().build();
                updateItemStack(25, capacityItem);
            }
            if (session.getFuel() != 0.0F) {
                fuelItem = new ItemStackBuilder(fuelItem).lore(ListUtil.singleton(String.valueOf(session.getFuel()))).grow().build();
                updateItemStack(32, fuelItem);
            }
            if (session.getId() != null && !session.getId().equalsIgnoreCase("id")) {
                idItem = new ItemStackBuilder(idItem).lore(ListUtil.singleton(session.getId())).grow().build();
                updateItemStack(19, idItem);
            }
            if (session.getName() != null && !session.getName().equalsIgnoreCase("name")) {
                nameItem = new ItemStackBuilder(nameItem).lore(ListUtil.singleton(session.getName())).grow().build();
                updateItemStack(21, nameItem);
            }
            if (session.getMaxSpeed() != null) {
                speedItem = new ItemStackBuilder(speedItem).lore(ListUtil.singleton(session.getMaxSpeed().getLabel())).grow().build();
                updateItemStack(23, speedItem);
            }
            if (session.getLores() != null) {
                loreItem = new ItemStackBuilder(loreItem).lore(session.getLores()).grow().build();
                updateItemStack(28, loreItem);
            }

            menu.update();
        });
    }

    private void addCreateModelSlot(Player player, EditModelData session) {
        ItemStack createItem = ItemStackBuilder.of(Material.END_CRYSTAL, 1).name(ChatColor.GREEN + "追加する").build();
        addSlot(new Slot(49, createItem, event -> {
            InventoryMenu.of(player).ifPresent(menu -> menu.flip(player, ModelMenuIndex.CONFIRM.value()));
        }));

    }
}
