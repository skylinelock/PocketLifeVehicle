package dev.sky_lock.mocar.gui.contents;

import com.google.common.collect.ImmutableList;
import dev.sky_lock.menu.InventoryMenu;
import dev.sky_lock.menu.MenuContents;
import dev.sky_lock.menu.Slot;
import dev.sky_lock.mocar.gui.*;
import dev.sky_lock.mocar.item.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author sky_lock
 */

public class ModelSettingContents extends MenuContents {
    private final Player player;
    private ItemStack carItem = ItemStackBuilder.of(Material.SLIME_BALL, 1).name("Item").build();
    private ItemStack capacityItem = ItemStackBuilder.of(Material.CHEST_MINECART, 1).name("Capacity").build();
    private ItemStack fuelItem = ItemStackBuilder.of(Material.COAL_BLOCK, 1).name("Fuel").build();
    private ItemStack idItem = ItemStackBuilder.of(Material.EMERALD, 1).name("Id").build();
    private ItemStack nameItem = ItemStackBuilder.of(Material.NAME_TAG, 1).name("Name").build();
    private ItemStack speedItem = ItemStackBuilder.of(Material.DIAMOND, 1).name("MaxSpeed").build();
    private ItemStack loreItem = ItemStackBuilder.of(Material.SIGN, 1).name("Lore").build();
    private ItemStack steeringItem = ItemStackBuilder.of(Material.SADDLE, 1).name("Steering").build();
    private ItemStack collideItem = ItemStackBuilder.of(Material.BEACON, 1).name("CollideBox").build();
    private ItemStack heightItem = ItemStackBuilder.of(Material.PURPUR_STAIRS, 1).name("Height").build();
    private ItemStack soundItem = ItemStackBuilder.of(Material.NOTE_BLOCK, 1).name("Sound").lore(ImmutableList.of(ChatColor.RED + "Coming soon")).build();
    private ItemStack createItem = ItemStackBuilder.of(Material.END_CRYSTAL, 1).name(ChatColor.GREEN + "追加する").build();


    public ModelSettingContents(Player player) {
        this.player = player;
        EditSessions.of(player.getUniqueId()).ifPresent(session -> {
            this.addSlot(new Slot(3, ItemStackBuilder.of(Material.ENDER_PEARL, 1).name(ChatColor.RED + "戻る").build(), event -> {
                InventoryMenu.of(player).ifPresent(menu -> menu.flip(player, ModelMenuIndex.MAIN_MENU.value()));
                EditSessions.destroy(player.getUniqueId());
            }));

            this.addSlot(new Slot(4, ItemStackBuilder.of(Material.ENDER_EYE, 1).name(ChatColor.RED + "閉じる").build(), event -> {
                InventoryMenu.of(player).ifPresent(menu -> menu.close((Player) event.getWhoClicked()));
                EditSessions.destroy(player.getUniqueId());
            }));

            this.addSlot(new Slot(11, idItem, event -> {
                InventoryMenu.of(player).ifPresent(menu -> {
                    StringEditor.open(player, StringEditor.Type.ID, (ModelSettingMenu) menu);
                });
            }));

            this.addSlot(new Slot(13, nameItem, event -> {
                InventoryMenu.of(player).ifPresent(menu -> {
                    StringEditor.open(player, StringEditor.Type.NAME, (ModelSettingMenu) menu);
                });
            }));

            this.addSlot(new Slot(15, speedItem, event -> {
                InventoryMenu.of(player).ifPresent(menu -> {
                    menu.flip(player, ModelMenuIndex.SPEED.value());
                });
            }));

            this.addSlot(new Slot(20, loreItem, event -> {
                new LoreEditor(player).open();
            }));

            this.addSlot(new Slot(22, carItem, event -> {
                InventoryMenu.of(player).ifPresent(menu -> menu.flip(player, ModelMenuIndex.CAR_ITEM.value()));
            }));

            this.addSlot(new Slot(24, capacityItem, event -> {
                InventoryMenu.of(player).ifPresent(menu -> menu.flip(player, ModelMenuIndex.CAPACITY.value()));
            }));

            this.addSlot(new Slot(29, fuelItem, event -> {
                InventoryMenu.of(player).ifPresent(menu -> menu.flip(player, ModelMenuIndex.FUEL.value()));
            }));

            this.addSlot(new Slot(31, collideItem, event -> {
                InventoryMenu.of(player).ifPresent(menu -> menu.flip(player, ModelMenuIndex.COLLIDE_BOX.value()));
            }));

            this.addSlot(new Slot(49, createItem, event -> {
                InventoryMenu.of(player).ifPresent(menu -> menu.flip(player, ModelMenuIndex.CONFIRM.value()));
            }));
        });
    }

    @Override
    public void onFlip(InventoryMenu menu) {
        EditSessions.of(player.getUniqueId()).ifPresent(session -> {
            if (session.getCarItem() != null) {
                int damage = session.getCarItem().getDamage();
                List<String> details = ImmutableList.of(session.getCarItem().getType().toString(), String.valueOf(damage));
                carItem = new ItemStackBuilder(carItem).lore(details).grow().build();
                updateItemStack(22, carItem);
            }
            if (session.getCapacity() != null) {
                capacityItem = new ItemStackBuilder(capacityItem).lore(ImmutableList.of(String.valueOf(session.getCapacity()))).grow().build();
                updateItemStack(24, capacityItem);
            }
            if (session.getFuel() != 0.0F) {
                fuelItem = new ItemStackBuilder(fuelItem).lore(ImmutableList.of(String.valueOf(session.getFuel()))).grow().build();
                updateItemStack(29, fuelItem);
            }
            if (session.getId() != null && !session.getId().equalsIgnoreCase("id")) {
                idItem = new ItemStackBuilder(idItem).lore(ImmutableList.of(session.getId())).grow().build();
                updateItemStack(11, idItem);
            }
            if (session.getName() != null && !session.getName().equalsIgnoreCase("name")) {
                nameItem = new ItemStackBuilder(nameItem).lore(ImmutableList.of(session.getName())).grow().build();
                updateItemStack(13, nameItem);
            }
            if (session.getMaxSpeed() != null) {
                speedItem = new ItemStackBuilder(speedItem).lore(ImmutableList.of(session.getMaxSpeed().getLabel())).grow().build();
                updateItemStack(15, speedItem);
            }
            if (session.getLores() != null) {
                loreItem = new ItemStackBuilder(loreItem).lore(session.getLores()).grow().build();
                updateItemStack(20, loreItem);
            }
            if (session.getCollideHeight() != 0.0F && session.getCollideBaseSide() != 0.0F) {
                collideItem = new ItemStackBuilder(collideItem).lore(ImmutableList.of(String.valueOf(session.getCollideBaseSide()), String.valueOf(session.getCollideHeight()))).grow().build();
                updateItemStack(31, collideItem);
            }

            menu.update();
        });
    }

}
