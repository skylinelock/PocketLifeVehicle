package dev.sky_lock.pocketlifevehicle.gui.contents;

import dev.sky_lock.menu.InventoryMenu;
import dev.sky_lock.menu.MenuContents;
import dev.sky_lock.menu.Slot;
import dev.sky_lock.menu.ToggleSlot;
import dev.sky_lock.pocketlifevehicle.PLVehicle;
import dev.sky_lock.pocketlifevehicle.gui.*;
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder;
import dev.sky_lock.pocketlifevehicle.vehicle.model.ModelList;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.Objects;

/**
 * @author sky_lock
 */

public class ModelSettingContents extends MenuContents {
    private final Player player;

    private final short closeSlot = 4;
    private final short idSlot = 11;
    private final short nameSlot = 13;
    private final short loreSlot = 15;
    private final short fuelSlot = 20;
    private final short speedSlot = 22;
    private final short capacitySlot = 24;
    private final short steeringSlot = 29;
    private final short itemSlot = 31;
    private final short collideSlot = 33;
    private final short standSlot = 38;
    private final short heightSlot = 40;
    private final short soundSlot = 42;

    private final short removeSlot = 46;
    private final short makeSlot = 49;

    private ItemStack idItem = ItemStackBuilder.of(Material.EMERALD, 1).name("ID").build();
    private ItemStack nameItem = ItemStackBuilder.of(Material.NAME_TAG, 1).name("名前").build();
    private ItemStack loreItem = ItemStackBuilder.of(Material.OAK_SIGN, 1).name("説明").build();

    private ItemStack fuelItem = ItemStackBuilder.of(Material.COAL_BLOCK, 1).name("燃料上限").build();
    private ItemStack speedItem = ItemStackBuilder.of(Material.DIAMOND, 1).name("最高速度").build();
    private ItemStack capacityItem = ItemStackBuilder.of(Material.SADDLE, 1).name("乗車人数").build();
    private ItemStack steeringItem = ItemStackBuilder.of(Material.LEAD, 1).name("ステアリング").lore(ChatColor.RED + "Coming soon").build();

    private ItemStack itemOptionItem = ItemStackBuilder.of(Material.ITEM_FRAME, 1).name("アイテム").build();

    private ItemStack collideItem = ItemStackBuilder.of(Material.BEACON, 1).name(ChatColor.RESET + "当たり判定").build();
    private ItemStack standSmallItem = ItemStackBuilder.of(Material.ARMOR_STAND, 1).name("大きさ").lore("小さい").build();
    private ItemStack standBigItem = ItemStackBuilder.of(Material.ARMOR_STAND, 1).name("大きさ").lore("大きい").build();
    private ItemStack heightItem = ItemStackBuilder.of(Material.IRON_HORSE_ARMOR, 1).name("座高").build();
    private ItemStack soundItem = ItemStackBuilder.of(Material.BELL, 1).name("エンジン音").lore(ChatColor.RED + "Coming soon").build();

    private ItemStack updateItem = ItemStackBuilder.of(Material.END_CRYSTAL, 1).name(ChatColor.GREEN + "更新する").build();
    private ItemStack removeItem = ItemStackBuilder.of(Material.BARRIER, 1).name(ChatColor.RED + "削除する").build();
    private ItemStack createItem = ItemStackBuilder.of(Material.END_CRYSTAL, 1).name(ChatColor.GREEN + "追加する").build();

    public ModelSettingContents(Player player) {
        this.player = player;
        EditSessions.of(player.getUniqueId()).ifPresent(session -> {
            this.addSlot(new Slot(closeSlot, ItemStackBuilder.of(Material.ENDER_EYE, 1).name(ChatColor.RED + "閉じる").build(), event -> {
                InventoryMenu.of(player).ifPresent(menu -> menu.close(player));
                EditSessions.destroy(player.getUniqueId());
            }));

            this.addSlot(new Slot(nameSlot, nameItem, event -> {
                InventoryMenu.of(player).ifPresent(menu -> {
                    StringEditor.open(player, StringEditor.Type.NAME, (ModelSettingMenu) menu);
                });
            }));

            this.addSlot(new Slot(loreSlot, loreItem, event -> {
                new LoreEditor(player).open();
            }));

            this.addSlot(new Slot(fuelSlot, fuelItem, event -> {
                InventoryMenu.of(player).ifPresent(menu -> menu.flip(player, ModelMenuIndex.FUEL.ordinal()));
            }));

            this.addSlot(new Slot(speedSlot, speedItem, event -> {
                InventoryMenu.of(player).ifPresent(menu -> {
                    menu.flip(player, ModelMenuIndex.SPEED.ordinal());
                });
            }));

            this.addSlot(new Slot(capacitySlot, capacityItem, event -> {
                InventoryMenu.of(player).ifPresent(menu -> menu.flip(player, ModelMenuIndex.CAPACITY.ordinal()));
            }));

            this.addSlot(new Slot(steeringSlot, steeringItem, event -> {

            }));

            this.addSlot(new Slot(itemSlot, itemOptionItem, event -> {
                InventoryMenu.of(player).ifPresent(menu -> menu.flip(player, ModelMenuIndex.ITEM_OPTION.ordinal()));
            }));

            this.addSlot(new Slot(collideSlot, collideItem, event -> {
                InventoryMenu.of(player).ifPresent(menu -> menu.flip(player, ModelMenuIndex.COLLIDE_BOX.ordinal()));
            }));

            this.addSlot(new Slot(heightSlot, heightItem, event -> {
                InventoryMenu.of(player).ifPresent(menu -> StringEditor.open(player, StringEditor.Type.HEIGHT, (ModelSettingMenu) menu));
            }));

            this.addSlot(new Slot(soundSlot, soundItem, event -> {

            }));
        });
    }

    @Override
    public void onFlip(InventoryMenu menu) {
        EditSessions.of(player.getUniqueId()).ifPresent(session -> {
            if (session.isJustEditing()) {
                this.removeSlot(removeSlot);
                this.addSlot(new Slot(removeSlot, removeItem, event -> {
                    String id = session.getId();
                    if (id == null || id.equals("")) {
                        return;
                    }
                    ModelList.remove(id);
                    player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + id + "を削除しました");
                    EditSessions.destroy(player.getUniqueId());
                    menu.close(player);
                }));
                this.removeSlot(makeSlot);
                this.addSlot(new Slot(makeSlot, updateItem, event -> {
                    ModelList.remove(session.getId());
                    ModelList.add(session.generate());
                    player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + session.getId() + "を更新しました");
                    EditSessions.destroy(player.getUniqueId());
                    menu.close(player);
                }));
            } else {
                this.removeSlot(idSlot);
                this.addSlot(new Slot(idSlot, idItem, event -> {
                    InventoryMenu.of(player).ifPresent(m -> {
                        StringEditor.open(player, StringEditor.Type.ID, (ModelSettingMenu) menu);
                    });
                }));
                this.removeSlot(makeSlot);
                this.addSlot(new Slot(makeSlot, createItem, event -> {
                    ItemStack clicked = Objects.requireNonNull(event.getCurrentItem());

                    if (!session.verifyCompleted()) {
                        ItemMeta itemMeta = Objects.requireNonNull(clicked.getItemMeta());
                        itemMeta.setLore(session.unfilledOptionWarning());
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
                    ModelList.add(session.generate());
                    player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + "新しい車種を追加しました");
                    EditSessions.destroy(player.getUniqueId());
                    player.closeInventory();
                }));
            }
            this.removeSlot(standSlot);
            this.addSlot(new ToggleSlot(standSlot, !session.isBig(), standSmallItem, standBigItem, event -> {
                session.setBig(true);
            }, event -> {
                session.setBig(false);
            }));
            if (session.getId() != null && !session.getId().equalsIgnoreCase("id")) {
                idItem = ItemStackBuilder.of(idItem).lore(session.getId()).glow().build();
                updateItemStack(idSlot, idItem);
            }
            if (session.getName() != null && !session.getName().equalsIgnoreCase("name")) {
                nameItem = ItemStackBuilder.of(nameItem).lore(session.getName()).glow().build();
                updateItemStack(nameSlot, nameItem);
            }
            if (session.getLore() != null) {
                loreItem = ItemStackBuilder.of(loreItem).lore(session.getLore()).glow().build();
                updateItemStack(loreSlot, loreItem);
            }
            if (session.getMaxFuel() != 0.0F) {
                fuelItem = ItemStackBuilder.of(fuelItem).lore(String.valueOf(session.getMaxFuel())).glow().build();
                updateItemStack(fuelSlot, fuelItem);
            }
            if (session.getMaxSpeed() != null) {
                speedItem = ItemStackBuilder.of(speedItem).lore(session.getMaxSpeed().getLabel()).glow().build();
                updateItemStack(speedSlot, speedItem);
            }
            if (session.getCapacity() != null) {
                capacityItem = ItemStackBuilder.of(capacityItem).lore(String.valueOf(session.getCapacity().value())).glow().build();
                updateItemStack(capacitySlot, capacityItem);
            }
            if (session.getSteeringLevel() != null) {
                steeringItem = ItemStackBuilder.of(steeringItem).lore(session.getSteeringLevel().name()).glow().build();
                updateItemStack(steeringSlot, steeringItem);
            }
            if (session.isItemValid() && session.getPosition() != null) {
                int modelId = session.getItemId();
                itemOptionItem = ItemStackBuilder.of(itemOptionItem).lore(session.getItemType().name(), String.valueOf(modelId), session.getPosition().getLabel()).glow().build();
                updateItemStack(itemSlot, itemOptionItem);
            }
            if (session.getCollideHeight() != 0.0F && session.getCollideBaseSide() != 0.0F) {
                collideItem = ItemStackBuilder.of(collideItem).name(ChatColor.AQUA + "当たり判定").lore(String.valueOf(session.getCollideBaseSide()), String.valueOf(session.getCollideHeight())).glow().build();
                updateItemStack(collideSlot, collideItem);
            }

            if (session.getHeight() != 0.0F) {
                heightItem = ItemStackBuilder.of(heightItem).lore(Float.toString(session.getHeight())).glow().build();
                updateItemStack(heightSlot, heightItem);
            }
            if (session.getSound() != null) {
                soundItem = ItemStackBuilder.of(soundItem).lore(session.getSound().name()).build();
                updateItemStack(soundSlot, soundItem);
            }

            menu.update();
        });
    }

}
